document.addEventListener('DOMContentLoaded', function() {
    loadReservations();
    document.getElementById('reservationList').addEventListener('click', async function(event) {
        const tr = event.target.closest('tr');
        if (!tr) return;
        const id = tr.querySelector('td')?.textContent;
        if (!id) return;
        await openReservationModal(id);
    });

    // 반납 연장 버튼
    document.getElementById('extendReservationBtn').addEventListener('click', async function() {
        const id = document.getElementById('modalReservationId').textContent;
        if (!id) return;
        try {
            const response = await fetch(`/reservations/${id}`, { method: 'PUT' });
            if (!response.ok) {
                let msg = '반납 연장에 실패했습니다.';
                try {
                    const error = await response.json();
                    if (error && error.message) msg = error.message;
                } catch {}
                alert(msg);
                return;
            }
            alert('반납일자가 연장되었습니다.');
            closeReservationModal();
            loadReservations();
        } catch (e) {
            alert('반납 연장에 실패했습니다.');
        }
    });

    // 반납(삭제) 버튼
    document.getElementById('deleteReservationBtn').addEventListener('click', async function() {
        const id = document.getElementById('modalReservationId').textContent;
        if (!id) return;
        if (!confirm('정말 반납하시겠습니까?')) return;
        try {
            const response = await fetch(`/reservations/${id}`, { method: 'DELETE' });
            if (!response.ok) throw new Error('반납에 실패했습니다.');
            alert('반납이 완료되었습니다.');
            closeReservationModal();
            loadReservations();
        } catch (e) {
            alert('반납에 실패했습니다.');
        }
    });
});

async function openReservationModal(id) {
    try {
        const response = await fetch(`/reservations/${id}`);
        if (!response.ok) throw new Error('상세 정보를 불러오지 못했습니다.');
        const data = await response.json();
        document.getElementById('modalReservationId').textContent = data.id ?? '';
        document.getElementById('modalBookTitle').textContent = data.bookResponse?.title ?? '';
        document.getElementById('modalBookAuthor').textContent = data.bookResponse?.author ?? '';
        document.getElementById('modalBookPublisher').textContent = data.bookResponse?.publisher ?? '';
        document.getElementById('modalBookPubdate').textContent = data.bookResponse?.pubdate ?? '';
        document.getElementById('modalBookIsbn').textContent = data.bookResponse?.isbn ?? '';
        document.getElementById('modalBookDescription').textContent = data.bookResponse?.description ?? '';
        document.getElementById('modalReserveDate').textContent = data.reserveDate ?? '';
        document.getElementById('modalReturnDate').textContent = data.returnDate ?? '';
        document.getElementById('modalStatus').textContent = data.status ?? '';

        // 상태에 따라 버튼 표시/숨김
        const isReturned = (data.status === '반납');
        document.getElementById('extendReservationBtn').style.display = isReturned ? 'none' : '';
        document.getElementById('deleteReservationBtn').style.display = isReturned ? 'none' : '';

        document.getElementById('reservationModal').style.display = 'flex';
        setTimeout(() => document.getElementById('reservationModal').classList.add('show'), 10);
    } catch (e) {
        alert('상세 정보를 불러오지 못했습니다.');
    }
}

function closeReservationModal() {
    const modal = document.getElementById('reservationModal');
    modal.classList.remove('show');
    setTimeout(() => { modal.style.display = 'none'; }, 300);
}

async function loadReservations() {
    try {
        const response = await fetch('/reservations');
        if (!response.ok) {
            throw new Error('예약 목록을 불러오는데 실패했습니다.');
        }
        
        const reservations = await response.json();
        const reservationListContainer = document.getElementById('reservationList');
        reservationListContainer.innerHTML = '';
        
        if (reservations.length === 0) {
            reservationListContainer.innerHTML = '<p class="text-center">예약한 도서가 없습니다.</p>';
            return;
        }
        
        reservations.forEach(reservation => {
            const reservationElement = createReservationElement(reservation);
            reservationListContainer.appendChild(reservationElement);
        });
    } catch (error) {
        console.error('예약 목록 로딩 중 오류 발생:', error);
        alert('예약 목록을 불러오는데 실패했습니다.');
    }
}

function createReservationElement(reservation) {
    const tr = document.createElement('tr');
    tr.innerHTML = `
        <td>${reservation.id ?? ''}</td>
        <td>${reservation.title ?? ''}</td>
        <td>${reservation.author ?? ''}</td>
        <td>${reservation.publisher ?? ''}</td>
        <td>${reservation.reserveDate ?? ''}</td>
        <td>${reservation.returnDate ?? ''}</td>
        <td>${reservation.status ?? ''}</td>
    `;
    return tr;
}
