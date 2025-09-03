document.addEventListener('DOMContentLoaded', function() {
    loadBooks();

    // 예약 버튼 클릭 이벤트 위임
    document.getElementById('bookList').addEventListener('click', async function(event) {
        if (event.target.classList.contains('reserve-btn')) {
            const bookId = event.target.getAttribute('data-book-id');
            if (!bookId) return;

            // 현재 날짜 구하기
            const now = new Date();
            const reserveDate = now.toLocaleDateString('ko-KR', {
                timeZone: 'Asia/Seoul',
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            }).replace(/\. /g, '-').replace('.', '').trim();

            console.log(reserveDate);

            const reservation = {
                bookId: Number(bookId),
                reserveDate
            };

            try {
                const response = await fetch(`/reservations`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(reservation)
                });
                if (response.ok) {
                    alert('예약이 완료되었습니다.');
                    loadBooks(); // 예약 후 목록 갱신
                } else {
                    const error = await response.json();
                    alert(error.message || '예약에 실패했습니다.');
                }
            } catch (error) {
                alert('예약 요청 중 오류가 발생했습니다.');
            }
        }
    });
});

async function loadBooks() {
    // 로그인 여부 확인
    try {
        const loginCheckResponse = await fetch('/login/check');
        if (!loginCheckResponse.ok) {
            const bookListContainer = document.getElementById('bookList');
            bookListContainer.innerHTML = `
                <div class="no-books">
                    <p>로그인 후 이용 가능합니다.</p>
                </div>
            `;
            return;
        }
    } catch (e) {
        const bookListContainer = document.getElementById('bookList');
        bookListContainer.innerHTML = `
            <div class="no-books">
                <p>로그인 후 이용 가능합니다.</p>
            </div>
        `;
        return;
    }

    // 로그인된 경우에만 도서 목록 불러오기
    try {
        const response = await fetch('/reservations/available');
        if (!response.ok) {
            throw new Error('도서 목록을 불러오는데 실패했습니다.');
        }
        
        const books = await response.json();
        const bookListContainer = document.getElementById('bookList');
        bookListContainer.innerHTML = '';
        
        if (books.length === 0) {
            bookListContainer.innerHTML = `
                <div class="no-books">
                    <p>등록된 도서가 없습니다.</p>
                </div>
            `;
            return;
        }
        
        books.forEach(book => {
            const bookElement = createBookElement(book);
            bookListContainer.appendChild(bookElement);
        });
    } catch (error) {
        console.error('도서 목록 로딩 중 오류 발생:', error);
        alert('도서 목록을 불러오는데 실패했습니다.');
    }
}

function createBookElement(book) {
    const div = document.createElement('div');
    div.className = 'book-item';
    const info = book.bookResponse || {};
    // description 줄바꿈 처리
    let description = info.description || '';
    description = description.replace(/\n/g, '<br>');
    div.innerHTML = `
        <div class="book-item-row">
            <div class="book-image">
                ${info.image ? `<img src="${info.image}" alt="${info.title || '도서 이미지'}">` : ''}
            </div>
            <div class="book-info">
                <div class="book-title">${info.title || ''}</div>
                <div class="book-author">저자: ${info.author || ''}</div>
                <div class="book-publisher">출판사: ${info.publisher || ''}</div>
                <div class="book-isbn">ISBN: ${info.isbn || ''}</div>
                <div class="book-description" style="max-height: 80px; overflow-y: auto;">${description}</div>
                <div class="book-bottom-row" style="display: flex; align-items: center; justify-content: space-between;">
                    <div class="book-count">수량: ${book.availableCount} / ${book.totalCount}</div>
                    <button class="reserve-btn" data-book-id="${book.bookId}">예약</button>
                </div>
            </div>
        </div>
    `;
    return div;
}
