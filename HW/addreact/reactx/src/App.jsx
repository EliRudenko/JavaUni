import { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "./App.css";

function App() {
    const navigate = useNavigate();
    const location = useLocation();
    const token = location.state?.token;

    // --- Состояния ---
    const [file, setFile] = useState(null);
    const [uploadProgress, setUploadProgress] = useState(0);
    const [message, setMessage] = useState("");
    const [filesList, setFilesList] = useState([]);
    const [mainImage, setMainImage] = useState("https://cataas.com/cat");

    const [searchQuery, setSearchQuery] = useState("");
    const [searchResults, setSearchResults] = useState([]);

    // --- Проверка токена ---
    useEffect(() => {
        if (!token) navigate("/login");
    }, [token, navigate]);

    // --- Обработка выбора файла ---
    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    // --- Загрузка файла с прогрессом ---
    const handleUpload = async (e) => {
        e.preventDefault();
        if (!file) return;

        setMessage("");
        setUploadProgress(0);

        const formData = new FormData();
        formData.append("file", file);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:8080/api/upload");

        xhr.upload.onprogress = (event) => {
            if (event.lengthComputable) {
                const percent = Math.round((event.loaded / event.total) * 100);
                setUploadProgress(percent);
            }
        };

        xhr.onload = () => {
            setUploadProgress(100);
            setTimeout(() => setUploadProgress(0), 500);

            if (xhr.status === 200) {
                const result = JSON.parse(xhr.responseText);
                if (result.success === "true") {
                    setMessage(result.message);
                    setFilesList((prev) => [...prev, file.name]);

                    if (file.type.startsWith("image/")) {
                        const reader = new FileReader();
                        reader.onload = (e) => setMainImage(e.target.result);
                        reader.readAsDataURL(file);
                    }

                    setFile(null);
                } else {
                    setMessage(result.message);
                }
            } else {
                setMessage("Помилка при завантаженні файлу");
            }
        };

        xhr.onerror = () => {
            setUploadProgress(0);
            setMessage("Помилка при завантаженні файлу");
        };

        xhr.send(formData);
    };

    // --- Поиск пользователей и файлов ---
    useEffect(() => {
        if (!searchQuery) {
            setSearchResults([]);
            return;
        }

        const timeoutId = setTimeout(() => {
            Promise.all([
                fetch(`http://localhost:8080/api/users/search?login=${searchQuery}`)
                    .then(res => res.json())
                    .catch(() => []),
                fetch(`http://localhost:8080/api/files/search?name=${searchQuery}`)
                    .then(res => res.json())
                    .catch(() => [])
            ])
                .then(([users, files]) => {
                    const results = [
                        ...users.map(u => u.login),
                        ...files.map(f => f.filename)
                    ];
                    setSearchResults(results);
                });
        }, 300);

        return () => clearTimeout(timeoutId);
    }, [searchQuery]);

    return (
        <div className="app-container">
            <div className="image-card">
                <h1 className="form-title">Ласкаво просимо на головну сторінку!</h1>

                <div className="image-gallery">
                    <img src={mainImage} alt="Головне зображення" className="gallery-img" />
                </div>

                <form onSubmit={handleUpload}>
                    <input type="file" onChange={handleFileChange} />
                    <button type="submit">Завантажити файл</button>
                </form>

                {uploadProgress > 0 && (
                    <div className="progress-bar">
                        <div
                            className="progress-fill"
                            style={{ width: `${uploadProgress}%` }}
                        ></div>
                    </div>
                )}

                {message && <p>{message}</p>}

                {filesList.length > 0 && (
                    <div style={{ marginTop: "20px" }}>
                        <h3>Список завантажених файлів:</h3>
                        <table className="files-table">
                            <thead>
                            <tr>
                                <th>№</th>
                                <th>Назва файлу</th>
                            </tr>
                            </thead>
                            <tbody>
                            {filesList.map((f, index) => (
                                <tr key={index}>
                                    <td>{index + 1}</td>
                                    <td>{f}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}

                {/* Поле поиска */}
                <div style={{ marginTop: "30px", textAlign: "left" }}>
                    <label>Пошук користувача або файлу:</label>
                    <input
                        type="text"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        placeholder="Введіть логін або назву файлу..."
                        style={{
                            width: "100%",
                            padding: "8px",
                            marginTop: "5px",
                            borderRadius: "5px",
                            border: "1px solid #ccc"
                        }}
                    />
                    {searchResults.length > 0 && (
                        <ul style={{ marginTop: "10px" }}>
                            {searchResults.map((item, idx) => (
                                <li key={idx}>{item}</li>
                            ))}
                        </ul>
                    )}
                </div>
            </div>
        </div>
    );
}

export default App;
