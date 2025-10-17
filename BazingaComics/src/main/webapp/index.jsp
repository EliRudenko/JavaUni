<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bazinga Comics</title>

    <!-- Подключаем CSS -->
    <link rel="stylesheet" href="css/styles.css">

    <!-- Подключаем иконки Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div class="top-bar">
    <div class="top-left">
        <a href="login.jsp"><i class="fas fa-lock-open"></i> SIGN IN</a>
        <a href="register.jsp"><i class="fas fa-user"></i> SIGN UP</a>
    </div>
    <div class="top-center">
        <img class="logo" src="https://www.pro-bems.com/IMAGES/images/FIGMUGTBBSUPLOGO/m/FIGMUGTBBSUPLOGO_6.jpg" alt="Bazinga Logo">
    </div>
    <div class="top-right">
        <button><i class="fas fa-star"></i> BAZINGA UNLIMITED</button>
        <button><i class="fas fa-search"></i></button>
    </div>
</div>

<!-- Navigation -->
<nav class="main-nav">
    <ul>
        <li><a href="#news">NEWS</a></li>
        <li><a href="catalog.jsp">COMICS</a></li>
        <li><a href="#characters">CHARACTERS</a></li>
        <li><a href="#movies">MOVIES</a></li>
        <li><a href="#tv-shows">TV SHOWS</a></li>
        <li><a href="#games">GAMES</a></li>
        <li><a href="#videos">VIDEOS</a></li>
        <li><a href="#more">MORE</a></li>
    </ul>
</nav>

<!-- Hero Banner Carousel -->
<section class="hero-banner" id="home">
    <div class="carousel">
        <div class="carousel-item active">
            <div class="carousel-image">
                <img src="https://blog.yakaboo.ua/wp-content/uploads/2016/11/%D0%BE%D0%B1%D0%BA%D0%BB%D0%B0%D0%B4%D0%B8%D0%BD%D0%BA%D0%B0-2-793x509.jpg" alt="Hero Poster">
            </div>
            <div class="carousel-text">
                <h2>YOUR COMPLETE GUIDE TO 'HEROES OF BAZINGA'</h2>
                <button class="cta-button">READ MORE</button>
            </div>
        </div>
        <div class="carousel-item">
            <div class="carousel-image">
                <img src="https://geekach.com.ua/content/uploads/images/ahr0cdovl3d3dy5uzxdzyxjhbweuy29tl2ltywdlcy9plzawmc8xmzevodq2l2kwmi9iyxrtyw4tc3vwzxjtyw4tc2vszmlllxzhcmlhbnquanbn.jpg" alt="New Series">
            </div>
            <div class="carousel-text">
                <h2>NEW COMIC SERIES LAUNCHING THIS MONTH</h2>
                <button class="cta-button">EXPLORE NOW</button>
            </div>
        </div>
        <div class="carousel-item">
            <div class="carousel-image">
                <img src="https://i.imgur.com/wuuFrX0.jpg" alt="Digital Comics">
            </div>
            <div class="carousel-text">
                <h2>UNLIMITED ACCESS TO DIGITAL COMICS</h2>
                <button class="cta-button">JOIN NOW</button>
            </div>
        </div>
    </div>
    <div class="carousel-dots">
        <span class="dot active"></span>
        <span class="dot"></span>
        <span class="dot"></span>
    </div>
</section>

<!-- Quick Links -->
<section class="quick-links" id="more">
    <h3>EXPLORE BAZINGA UNIVERSE</h3>
    <div class="quick-links-grid">
        <a href="#characters" class="quick-link-card">
            <i class="fas fa-mask"></i>
            <h4>CHARACTERS</h4>
        </a>
        <a href="#movies" class="quick-link-card">
            <i class="fas fa-film"></i>
            <h4>MOVIES</h4>
        </a>
        <a href="#tv-shows" class="quick-link-card">
            <i class="fas fa-tv"></i>
            <h4>TV SHOWS</h4>
        </a>
        <a href="#games" class="quick-link-card">
            <i class="fas fa-gamepad"></i>
            <h4>GAMES</h4>
        </a>
        <a href="#news" class="quick-link-card">
            <i class="fas fa-newspaper"></i>
            <h4>NEWS</h4>
        </a>
        <a href="#videos" class="quick-link-card">
            <i class="fas fa-play-circle"></i>
            <h4>VIDEOS</h4>
        </a>
        <a href="catalog.jsp" class="quick-link-card">
            <i class="fas fa-book"></i>
            <h4>DIGITAL COMICS</h4>
        </a>
        <a href="#new-week" class="quick-link-card">
            <i class="fas fa-fire"></i>
            <h4>NEW RELEASES</h4>
        </a>
    </div>
</section>

<!-- Sections -->
<section class="section-container" id="new-week">
    <div class="section-header">
        <h2>NEW THIS WEEK</h2>
        <a href="#comics">VIEW ALL <i class="fas fa-arrow-right"></i></a>
    </div>
    <div class="new-week-grid">
        <div class="comic-card">
            <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/9/b0/68c9b5d6e01e4/portrait_uncanny.jpg">
            <div class="comic-card-content">
                <div class="comic-title">X-MEN: AGE OF REVELATION OVERTURE (2025) #1</div>
                <div class="comic-meta">Mackay, Stegman</div>
                <div class="rating">★★★★★</div>
            </div>
        </div>
        <div class="comic-card">
            <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/4/50/68c9b5c875219/portrait_uncanny.jpg">
            <div class="comic-card-content">
                <div class="comic-title">Spirits of Violence (2025) #1</div>
                <div class="comic-meta">Pirzada, Davidson, Lim</div>
                <div class="rating">★★★★☆</div>
            </div>
        </div>
        <div class="comic-card">
            <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/7/20/68c9b5c87451d/portrait_uncanny.jpg">
            <div class="comic-card-content">
                <div class="comic-title">Spider-Man Noir (2025) #1</div>
                <div class="comic-meta">Larsen, Broccardo, Meo</div>
                <div class="rating">★★★★★</div>
            </div>
        </div>
        <div class="comic-card">
            <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/9/b0/68c9b5d6dd313/portrait_uncanny.jpg">
            <div class="comic-card-content">
                <div class="comic-title">X-MEN: THE UNDERTOW (2025) #1</div>
                <div class="comic-meta">Mckone</div>
                <div class="rating">★★★★☆</div>
            </div>
        </div>
        <div class="comic-card">
            <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/2/04/68c9b5d6bbe7f/portrait_uncanny.jpg">
            <div class="comic-card-content">
                <div class="comic-title">WHITE TIGER: REBORN (2025) #1</div>
                <div class="comic-meta">Hawthorne</div>
                <div class="rating">★★★★☆</div>
            </div>
        </div>
        <div class="comic-card">
            <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/8/d0/6887a01ea14aa/portrait_uncanny.jpg">
            <div class="comic-card-content">
                <div class="comic-title">MARVEL/DC: SPIDER-BOY FACSIMILE EDITION (2025) #1</div>
                <div class="comic-meta">Kesel, Wieringo</div>
                <div class="rating">★★★☆☆</div>
            </div>
        </div>
        <div class="comic-card">
            <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/a/70/68c9b5d794e2f/portrait_uncanny.jpg">
            <div class="comic-card-content">
                <div class="comic-title">New Avengers (2025) #5</div>
                <div class="comic-meta">Humphries, Palma, Segovia</div>
                <div class="rating">★★★★☆</div>
            </div>
        </div>
        <div class="comic-card">
            <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/7/40/68c9b5c876a90/portrait_uncanny.jpg">
            <div class="comic-card-content">
                <div class="comic-title">Star Wars (2025) #6</div>
                <div class="comic-meta">Segura, Noto</div>
                <div class="rating">★★★☆☆</div>
            </div>
        </div>
        <div class="comic-card">
            <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/d/30/68c9b5d9da81f/portrait_uncanny.jpg">
            <div class="comic-card-content">
                <div class="comic-title">Red Hulk (2025) #9</div>
                <div class="comic-meta">Percy, Guzman, Shaw</div>
                <div class="rating">★★★★☆</div>
            </div>
        </div>
        <div class="comic-card">
            <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/a/a0/68c9b5c87d6f6/portrait_uncanny.jpg">
            <div class="comic-card-content">
                <div class="comic-title">Ultimate Wolverine (2025) #10</div>
                <div class="comic-meta">Condon, Cappuccio</div>
                <div class="rating">★★★☆☆</div>
            </div>
        </div>
    </div>
</section>

<!-- Best Selling Carousel -->
<!-- Best Selling Carousel -->
<section class="section-container" id="comics">
    <div class="section-header">
        <h2>BEST SELLING DIGITAL COMICS</h2>
        <a href="#new-week">BROWSE ALL <i class="fas fa-arrow-right"></i></a>
    </div>
    <div class="carousel-wrapper">
        <button class="scroll-left"><i class="fas fa-chevron-left"></i></button>
        <div class="comics-carousel best-selling-carousel">
            <div class="comic-card">
                <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/7/03/667c7744d0240/portrait_uncanny.jpg">
                <div class="comic-card-content">
                    <div class="comic-title">Moon Knight: Fist of Khonshu (2024)</div>
                    <div class="comic-meta">Kelly, Cappuccio, Gist</div>
                    <div class="rating">★★★★☆</div>
                </div>
            </div>
            <div class="comic-card">
                <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/1/d0/6259cb8887864/portrait_uncanny.jpg">
                <div class="comic-card-content">
                    <div class="comic-title">Captain America (2018)</div>
                    <div class="comic-meta">Kelly, Iulis</div>
                    <div class="rating">★★★★☆</div>
                </div>
            </div>
            <div class="comic-card">
                <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/b/e0/633cefc1e12b5/portrait_uncanny.jpg">
                <div class="comic-card-content">
                    <div class="comic-title">Miracleman (2022)</div>
                    <div class="comic-meta">Templeton, Davis</div>
                    <div class="rating">★★★☆☆</div>
                </div>
            </div>
            <div class="comic-card">
                <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/3/f0/5f59430a0898d/portrait_uncanny.jpg">
                <div class="comic-card-content">
                    <div class="comic-title">Immortal Hulk (2020)</div>
                    <div class="comic-meta">Ewing, Iulis</div>
                    <div class="rating">★★★★★</div>
                </div>
            </div>
            <div class="comic-card">
                <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/5/e0/5ff32d1037c32/portrait_uncanny.jpg">
                <div class="comic-card-content">
                    <div class="comic-title">Captain America #117: Facsimile Edition (2021)</div>
                    <div class="comic-meta">Lee, Colan</div>
                    <div class="rating">★★★★☆</div>
                </div>
            </div>

            <!-- Скрытые карточки, появляющиеся при прокрутке -->
            <div class="comic-card">
                <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/c/b0/5c6c58de533ec/portrait_uncanny.jpg">
                <div class="comic-card-content">
                    <div class="comic-title">Iron Man: Crash (1987)</div>
                    <div class="comic-meta">Saenz</div>
                    <div class="rating">★★★★★</div>
                </div>
            </div>
            <div class="comic-card">
                <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/6/90/5ce308621ee94/portrait_uncanny.jpg">
                <div class="comic-card-content">
                    <div class="comic-title">Silver Surfer (1987)</div>
                    <div class="comic-meta">Dematteis, Garney</div>
                    <div class="rating">★★★★☆</div>
                </div>
            </div>
            <div class="comic-card">
                <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/7/10/68c9b5d62d735/portrait_uncanny.jpg">
                <div class="comic-card-content">
                    <div class="comic-title">Venom: Dark Origin (2024)</div>
                    <div class="comic-meta">Costa, Sandoval</div>
                    <div class="rating">★★★★☆</div>
                </div>
            </div>
            <div class="comic-card">
                <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/8/d0/6887a01ea14aa/portrait_uncanny.jpg">
                <div class="comic-card-content">
                    <div class="comic-title">Spider-Boy (2025)</div>
                    <div class="comic-meta">Kesel, Wieringo</div>
                    <div class="rating">★★★☆☆</div>
                </div>
            </div>
            <div class="comic-card">
                <img src="https://cdn.marvel.com/u/prod/marvel/i/mg/7/40/68c9b5c876a90/portrait_uncanny.jpg">
                <div class="comic-card-content">
                    <div class="comic-title">Star Wars (2025)</div>
                    <div class="comic-meta">Segura, Noto</div>
                    <div class="rating">★★★☆☆</div>
                </div>
            </div>
        </div>
        <button class="scroll-right"><i class="fas fa-chevron-right"></i></button>
    </div>
</section>

<!-- Footer -->
<footer class="footer">
    <div class="footer-top">
        <div class="footer-logo">
            <img src="https://image.tmdb.org/t/p/original/w4SDOkKPIQKQ1swVPqpsdqEybk2.png" alt="Logo">
        </div>

        <div class="footer-links">
            <div class="footer-column">
                <h4>COMICS</h4>
                <ul>
                    <li><a href="#">Browse All</a></li>
                    <li><a href="#">New Releases</a></li>
                    <li><a href="#">Digital Comics</a></li>
                    <li><a href="#">Redeem Codes</a></li>
                </ul>
            </div>
            <div class="footer-column">
                <h4>CHARACTERS</h4>
                <ul>
                    <li><a href="#">Spider-Man</a></li>
                    <li><a href="#">Iron Man</a></li>
                    <li><a href="#">Hulk</a></li>
                    <li><a href="#">Wolverine</a></li>
                </ul>
            </div>
            <div class="footer-column">
                <h4>ABOUT MARVEL</h4>
                <ul>
                    <li><a href="#">Help/FAQs</a></li>
                    <li><a href="#">Careers</a></li>
                    <li><a href="#">Internships</a></li>
                    <li><a href="#">Advertising</a></li>
                </ul>
            </div>
            <div class="footer-column">
                <h4>FOLLOW US</h4>
                <div class="social-icons">
                    <a href="#"><i class="fab fa-facebook-f"></i></a>
                    <a href="#"><i class="fab fa-twitter"></i></a>
                    <a href="#"><i class="fab fa-instagram"></i></a>
                    <a href="#"><i class="fab fa-youtube"></i></a>
                </div>
            </div>
        </div>
    </div>

    <div class="footer-bottom">
        <p>© 2025 Marvel Entertainment. All Rights Reserved.</p>
    </div>
</footer>

<script src="js/scripts.js"></script>
</body>
</html>