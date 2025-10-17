// Carousel Scroll
document.querySelectorAll('.scroll-left').forEach(btn=>{
    btn.addEventListener('click', ()=>{
        const carousel = btn.parentElement.querySelector('.comics-carousel');
        carousel.scrollBy({left:-920, behavior:'smooth'}); // 5 cards * 180 + gaps
    });
});
document.querySelectorAll('.scroll-right').forEach(btn=>{
    btn.addEventListener('click', ()=>{
        const carousel = btn.parentElement.querySelector('.comics-carousel');
        carousel.scrollBy({left:920, behavior:'smooth'});
    });
});





// HERO BANNER CAROUSEL
let currentIndex = 0;
const slides = document.querySelectorAll('.carousel-item');
const dots = document.querySelectorAll('.dot');

function showSlide(index) {
    slides.forEach((slide,i) => {
        slide.classList.toggle('active', i === index);
        dots[i].classList.toggle('active', i === index);
    });
    currentIndex = index;
}

function nextSlide() {
    let nextIndex = (currentIndex + 1) % slides.length;
    showSlide(nextIndex);
}

function prevSlide() {
    let prevIndex = (currentIndex - 1 + slides.length) % slides.length;
    showSlide(prevIndex);
}

dots.forEach((dot,i) => {
    dot.addEventListener('click', () => showSlide(i));
});

// автопереключение каждые 5 сек
setInterval(nextSlide, 5000);

// SCROLL CAROUSELS
document.querySelectorAll('.carousel-wrapper').forEach(wrapper => {
    const leftBtn = wrapper.querySelector('.scroll-left');
    const rightBtn = wrapper.querySelector('.scroll-right');
    const carousel = wrapper.querySelector('.comics-carousel');

    rightBtn.addEventListener('click', () => {
        carousel.scrollBy({ left: carousel.clientWidth, behavior: 'smooth' });
    });
    leftBtn.addEventListener('click', () => {
        carousel.scrollBy({ left: -carousel.clientWidth, behavior: 'smooth' });
    });
});





// BEST SELLING CAROUSEL SCROLL
document.querySelectorAll('.carousel-wrapper').forEach(wrapper => {
    const leftBtn = wrapper.querySelector('.scroll-left');
    const rightBtn = wrapper.querySelector('.scroll-right');
    const carousel = wrapper.querySelector('.comics-carousel');

    rightBtn.addEventListener('click', () => {
        carousel.scrollBy({ left: carousel.clientWidth, behavior: 'smooth' });
    });
    leftBtn.addEventListener('click', () => {
        carousel.scrollBy({ left: -carousel.clientWidth, behavior: 'smooth' });
    });
});
