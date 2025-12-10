import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './App.css';

function Register() {
    const [formData, setFormData] = useState({ login: '', password: '', confirmPassword: '' });
    const [responseMsg, setResponseMsg] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setResponseMsg('');

        if (formData.password !== formData.confirmPassword) {
            setResponseMsg('Паролі не співпадають');
            return;
        }

        setLoading(true);
        try {
            const res = await fetch('http://localhost:8080/api/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: formData.login, password: formData.password })
            });

            if (!res.ok) throw new Error('Помилка сервера');

            const result = await res.json(); //!!!
            if (result.success === "true") {
                navigate('/login'); //!!
            } else {
                setResponseMsg(result.message);
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
                <h1 className="form-title">Реєстрація</h1>
                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <label>Логін</label>
                        <input type="text" name="login" value={formData.login} onChange={handleChange} required disabled={loading}/>
                    </div>
                    <div className="input-group">
                        <label>Пароль</label>
                        <input type="password" name="password" value={formData.password} onChange={handleChange} required disabled={loading}/>
                    </div>
                    <div className="input-group">
                        <label>Підтвердження пароля</label>
                        <input type="password" name="confirmPassword" value={formData.confirmPassword} onChange={handleChange} required disabled={loading}/>
                    </div>
                    <button type="submit" disabled={loading}>{loading ? 'Реєстрація...' : 'Зареєструватися'}</button>
                    <Link to="/login"><button type="button" disabled={loading}>Повернутися до входу</button></Link>
                    {responseMsg && <p className="error">{responseMsg}</p>}
                </form>
            </div>
        </div>
    );
}

export default Register;
