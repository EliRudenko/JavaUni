import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Login.css';

function Login() {
    const [formData, setFormData] = useState({ login: '', password: '' });
    const [responseMsg, setResponseMsg] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setResponseMsg('');

        try {
            const res = await fetch('http://localhost:8080/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: formData.login, password: formData.password })
            });

            if (!res.ok) throw new Error('Помилка сервера');

            const result = await res.json(); //!!
            if (result.success === "true") {
                navigate('/main', { state: { token: result.token } }); //!!
            } else {
                setResponseMsg(result.message || 'Нема такого користувача');
            }
        } catch (err) {
            setResponseMsg('Сервер не працює: ' + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="app-container">
            <div className="form-card">
                <h1 className="form-title">Вхід</h1>
                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <label>Логін або пошта</label>
                        <input
                            type="text"
                            name="login"
                            value={formData.login}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        />
                    </div>
                    <div className="input-group">
                        <label>Пароль</label>
                        <input
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        />
                    </div>
                    <button type="submit" disabled={loading}>{loading ? 'Увійти...' : 'Увійти в систему'}</button>
                    <Link to="/register"><button type="button" disabled={loading}>Зареєструватися</button></Link>
                    {responseMsg && <p className="error">{responseMsg}</p>}
                </form>
            </div>
        </div>
    );
}

export default Login;
