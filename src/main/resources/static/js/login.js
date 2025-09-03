document.addEventListener('DOMContentLoaded', async function() {
    const navRight = document.getElementById('navRight');
    const headerNav = navRight && navRight.parentElement;
    // user-name을 위한 span이 이미 있으면 재사용, 없으면 새로 만듦
    let userNameSpan = document.getElementById('userNameSpan');
    if (!navRight) return;
    if (!userNameSpan && headerNav) {
        userNameSpan = document.createElement('span');
        userNameSpan.id = 'userNameSpan';
        userNameSpan.className = 'user-name';
        headerNav.insertBefore(userNameSpan, navRight);
    }
    try {
        const response = await fetch('/login/check');
        if (response.ok) {
            const user = await response.json();
            // 480px 이하: user-name은 navRight 바깥, 버튼만 navRight에
            if (window.innerWidth <= 480 && userNameSpan) {
                userNameSpan.textContent = `${user.name}님`;
                if (navRight) navRight.innerHTML = `
                    <a href="/my-reservations" class="nav-link">내 예약</a>
                    <a href="/logout" class="btn-secondary">로그아웃</a>
                `;
                userNameSpan.style.display = 'inline-block';
            } else {
                // 데스크탑: user-name + 버튼 모두 navRight에
                if (navRight) navRight.innerHTML = `
                    <span class="user-name">${user.name}님</span>
                    <a href="/my-reservations" class="nav-link">내 예약</a>
                    <a href="/logout" class="btn-secondary">로그아웃</a>
                `;
                if (userNameSpan) userNameSpan.style.display = 'none';
            }
        } else {
            if (navRight) navRight.innerHTML = `
                <a href="/login/github" class="btn-primary">로그인</a>
            `;
            if (userNameSpan) userNameSpan.style.display = 'none';
        }
    } catch (e) {
        if (navRight) navRight.innerHTML = `
            <a href="/login/github" class="btn-primary">로그인</a>
        `;
        if (userNameSpan) userNameSpan.style.display = 'none';
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const hamburgerBtn = document.getElementById('hamburgerBtn');
    const navRight = document.getElementById('navRight');
    if (hamburgerBtn && navRight) {
        hamburgerBtn.addEventListener('click', function() {
            navRight.classList.toggle('show');
        });
        // 바깥 클릭 시 닫기
        document.addEventListener('click', function(e) {
            if (window.innerWidth > 480) return;
            if (!navRight.classList.contains('show')) return;
            if (!navRight.contains(e.target) && e.target !== hamburgerBtn) {
                navRight.classList.remove('show');
            }
        });
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const navRight = document.getElementById('navRight');
    if (navRight) {
        navRight.addEventListener('click', async function(e) {
            const target = e.target;
            if (target.matches('a.btn-secondary[href="/logout"]')) {
                e.preventDefault();
                try {
                    const response = await fetch('/logout', { method: 'POST', credentials: 'same-origin' });
                    if (response.ok) {
                        window.location.href = '/';
                    } else {
                        alert('로그아웃에 실패했습니다.');
                    }
                } catch (err) {
                    alert('로그아웃 중 오류가 발생했습니다.');
                }
            }
        });
    }
});
