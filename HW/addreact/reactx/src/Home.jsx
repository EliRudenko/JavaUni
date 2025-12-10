import React from 'react';
import { Link } from 'react-router-dom';
import './Home.css';

function Home() {
    return (
        <div className="app-container">
            <div className="form-card">
                <h1 className="form-title">Добро пожаловать!</h1>
                <p>на главную страницу.</p>
                <div className="button-group">
                    <Link to="/login" className="submit-btn">Авторизация</Link>
                    <Link to="/register" className="submit-btn">Регистрация</Link>
                </div>

            </div>
        </div>
    );
}

export default Home;
