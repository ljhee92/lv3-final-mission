document.addEventListener('DOMContentLoaded', function() {
    const bookForm = document.getElementById('registerForm');
    bookForm.addEventListener('submit', handleBookSubmit);
});

async function searchBooks(event) {
    event.preventDefault();
    
    const searchInput = document.getElementById('searchInput');
    const query = searchInput.value.trim();
    
    console.log('검색어:', query);
    console.log('검색어 길이:', query.length);
    
    if (!query || query.length === 0) {
        alert('검색어를 입력해주세요.');
        searchInput.focus();
        return;
    }
    
    const url = `/admin/books?keyword=${query}`;
    console.log('요청 URL:', url);
    
    try {
        const response = await fetch(url);
        console.log('응답 상태:', response.status);
        
        if (!response.ok) {
            const errorData = await response.json();
            console.error('에러 응답:', errorData);
            throw new Error(errorData.message || '도서 검색에 실패했습니다.');
        }
        
        const books = await response.json();
        console.log('검색 결과:', books);
        
        const bookList = document.getElementById('bookList');
        const noBooks = document.getElementById('noBooks');
        
        bookList.innerHTML = '';
        
        if (!books || books.length === 0) {
            noBooks.style.display = 'block';
            return;
        }
        
        noBooks.style.display = 'none';
        books.forEach(book => {
            const bookElement = createBookElement(book);
            bookList.appendChild(bookElement);
        });
    } catch (error) {
        console.error('도서 검색 중 오류 발생:', error);
        alert(error.message || '도서 검색 중 오류가 발생했습니다.');
    }
}

function createBookElement(book) {
    const div = document.createElement('div');
    div.className = 'admin-book-item';
    div.innerHTML = `
        <div class="admin-book-content">
            <div class="admin-book-image">
                <img src="${book.image}" alt="${book.title}" onerror="this.src='/images/no-image.png'">
            </div>
            <div class="admin-book-info">
                <div class="admin-book-title">${book.title}</div>
                <div class="admin-book-detail">저자: ${book.author}</div>
                <div class="admin-book-detail">출판사: ${book.publisher}</div>
                <div class="admin-book-detail">ISBN: ${book.isbn}</div>
            </div>
        </div>
        <div class="admin-book-actions">
            <button class="btn btn-primary" onclick="showRegisterModal(${JSON.stringify(book).replace(/"/g, '&quot;')})">등록</button>
        </div>
    `;
    return div;
}

function showRegisterModal(book) {
    const modal = document.getElementById('registerModal');
    const form = document.getElementById('registerForm');
    
    // 폼 필드에 도서 정보 채우기
    document.getElementById('title').value = book.title;
    document.getElementById('author').value = book.author;
    document.getElementById('image').value = book.image;
    document.getElementById('publisher').value = book.publisher;
    document.getElementById('pubdate').value = book.pubdate || '';
    document.getElementById('isbn').value = book.isbn;
    document.getElementById('description').value = book.description || '';
    document.getElementById('quantity').value = 1;
    
    modal.classList.add('show');
}

async function handleBookSubmit(e) {
    e.preventDefault();
    
    // 한국 시간 기준 yyyy-MM-dd 날짜 생성
    const now = new Date();
    const regDateKST = new Intl.DateTimeFormat('ko-KR', {
        timeZone: 'Asia/Seoul',
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    }).format(now);
    const [year, month, day] = regDateKST.replace(/\./g, '').split(' ').filter(Boolean);
    const regDate = `${year}-${month}-${day}`;
    
    const formData = {
        title: document.getElementById('title').value,
        author: document.getElementById('author').value,
        image: document.getElementById('image').value,
        publisher: document.getElementById('publisher').value,
        pubdate: document.getElementById('pubdate').value,
        isbn: document.getElementById('isbn').value,
        description: document.getElementById('description').value,
        totalCount: parseInt(document.getElementById('quantity').value),
        regDate: regDate
    };
    
    try {
        const response = await fetch('/admin/books', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            alert('도서가 성공적으로 등록되었습니다.');
            closeModal();
            // 검색 결과 초기화
            document.getElementById('bookList').innerHTML = '';
            document.getElementById('searchInput').value = '';
            document.getElementById('noBooks').style.display = 'none';
        } else {
            const error = await response.json();
            alert(error.message || '도서 등록에 실패했습니다.');
        }
    } catch (error) {
        console.error('도서 등록 중 오류 발생:', error);
        alert('도서 등록 중 오류가 발생했습니다.');
    }
}

function closeModal() {
    const modal = document.getElementById('registerModal');
    modal.classList.remove('show');
    document.getElementById('registerForm').reset();
}
