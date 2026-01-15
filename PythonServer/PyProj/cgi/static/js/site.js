// ДЗ 8, 9, 12 — UI тестов, JWT header/payload, общий итог.
// Файл отвечает за работу кнопок на страницах /usertest и /ordertest.
class Base64 {
    static #textEncoder = new TextEncoder();
    static #textDecoder = new TextDecoder();

    // https://datatracker.ietf.org/doc/html/rfc4648
    static encode = (str) =>
        btoa(String.fromCharCode(...Base64.#textEncoder.encode(str)));

    static decode = (str) =>
        Base64.#textDecoder.decode(
            Uint8Array.from(atob(str), c => c.charCodeAt(0))
        );

    static encodeUrl = (str) =>
        this.encode(str)
            .replace(/\+/g, '-')
            .replace(/\//g, '_')
            .replace(/=/g, '');

    static decodeUrl = (str) =>
        this.decode(str.replace(/-/g, '+').replace(/_/g, '/'));

    static jwtEncodeBody = (header, payload) =>
        this.encodeUrl(JSON.stringify(header)) + '.' +
        this.encodeUrl(JSON.stringify(payload));

    static jwtDecodePayload = (jwt) =>
        JSON.parse(this.decodeUrl(jwt.split('.')[1]));

    static jwtDecodeHeader = (jwt) =>
        JSON.parse(this.decodeUrl(jwt.split('.')[0]));
}

document.addEventListener("DOMContentLoaded", initApiTests);

// Глобальное состояние тестов.
// Это нужно, чтобы в конце показывать общий итог по выполненным тестам.
const testState = {
    total: 0,
    passed: 0,
    failed: 0,
    details: []
};

function initApiTests() {
    // Подключаем кнопки для обычных API-тестов (user/order/discount).
    // Для order используем полный набор методов.
    const apiNames = ["user", "order", "discount"];
    const apiMethods = ["get", "post", "put", "patch", "delete"];
    for(let apiName of apiNames) {
        for(let apiMethod of apiMethods) {
            let btnId = `api-${apiName}-${apiMethod}-btn`;
            let btn = document.getElementById(btnId);
            if(btn) {
                btn.addEventListener("click", apiTestBtnClicked);
            }
        }
    }
    // Набор проверок "некорректной" аутентификации (ДЗ №8).
    // В каждой записи есть:
    // - endpoint и method: куда отправляем запрос
    // - headers: какие заголовки подставляем
    // - result: ID блока, куда пишем ответ
    // - expectedOk: ожидаем успех или ошибку
    // - title: человекочитаемое имя теста
    const errorTests = {
        "api-401-missing-header-btn": { 
            endpoint: "/user",
            method: "GET",
            headers: {}, 
            result: "api-missing-header-result",
            expectedOk: false,
            title: "Missing Authorization header"
        },
        "api-401-incorrect-scheme-btn": { 
            endpoint: "/user",
            method: "GET",
            headers: { "Authorization": "Bearer invalid_token" },
            result: "api-incorrect-scheme-result",
            expectedOk: false,
            title: "Wrong Authorization scheme"
        },
        "api-401-short-credentials-btn": { 
            endpoint: "/user",
            method: "GET",
            headers: { "Authorization": "Basic QQ==" },
            result: "api-short-credentials-result",
            expectedOk: false,
            title: "Short credentials"
        },
        "api-401-malformed-credentials-btn": { 
            endpoint: "/user",
            method: "GET",
            headers: { "Authorization": "Basic QWxhZG~~~RpbjpvcGVuIHNlc2FtZQ==" },
            result: "api-malformed-credentials-result",
            expectedOk: false,
            title: "Malformed base64 symbols"
        },
        "api-401-padding-error-btn": { 
            endpoint: "/user",
            method: "GET",
            headers: { "Authorization": "Basic YWRtaW46MTI" },
            result: "api-padding-error-result",
            expectedOk: false,
            title: "Base64 padding error"
        },
        "api-401-missing-colon-btn": { 
            endpoint: "/user",
            method: "GET",
            headers: { "Authorization": "Basic YWRtaW4xMjM=" },
            result: "api-missing-colon-result",
            expectedOk: false,
            title: "Missing ':' in decoded credentials"
        },
        "api-401-invalid-base64-btn": { 
            endpoint: "/user",
            method: "GET",
            headers: { "Authorization": "Basic dGVzd$pwYXNz" },
            result: "api-invalid-base64-result",
            expectedOk: false,
            title: "Invalid Base64 characters"
        }
    };

    Object.entries(errorTests).forEach(([btnId, params]) => {
        const btn = document.getElementById(btnId);
        if (btn) {
            btn.onclick = () =>
                runApiErrorTest(
                    params.endpoint,
                    params.method,
                    params.headers,
                    params.result,
                    params.expectedOk,
                    params.title
                );
        }
    });

    document.querySelector('[data-id="api-discount-get-btn"]').addEventListener("click", apiPassBtnClicked);
}

function apiPassBtnClicked(e) {
    // Отдельный обработчик для кнопки "Discount program",
    // так как у неё нестандартный data-path.
    const [prefix, apiName, apiMethod, _] = e.target.getAttribute("data-id").split('-');
    const resId = `${prefix}-${apiName}-${apiMethod}-result`;
    const td = document.querySelector(`[data-id="${resId}"]`);
    const path = e.target.getAttribute("data-path");

    const tokenElem = document.getElementById("token");
    const token = tokenElem ? tokenElem.innerText : null;
    if(td) {
        fetch(`/${apiName}${path}`, {
            method: apiMethod.toUpperCase(),
            headers: {
                "Access-Control-Allow-Origin": "cgi221.loc",
                "Custom-Header": "My Value",
                "Authorization": token == null || token.length == 0 
                    ? "Basic YWRtaW46YWRtaW4=" 
                    : `Bearer ${token}` // "admin" and password admin
            }
            
        }).then(r => {
            if (r.ok) {
                r.json().then(j => {
                    td.innerHTML = `<pre>${JSON.stringify(j, null, 4)}</pre>`;
                    if(j.meta.data_type == "token") {
                        document.getElementById("token").innerText = j.data;
                        const payloadBase64 = j.data.split('.')[1];
                        const payloadJson = JSON.parse(Base64.decodeUrl(payloadBase64));

                        document.getElementById("token-payload").innerHTML =
                            `<pre>${JSON.stringify(payloadJson, null, 4)}</pre>`;
                    }

                    const headerJson = Base64.jwtDecodeHeader(j.data);
                    document.getElementById("token-header").innerHTML =
                        `<pre>${JSON.stringify(headerJson, null, 4)}</pre>`;

                });
            }
            else{
                r.text().then(t => td.innerText = t);
            }
        })
    }
    else {
        throw "Container not found: " + resId;
    }
}

function apiTestBtnClicked(e) {
    // Основной обработчик для кнопок API.
    const [prefix, apiName, apiMethod, _] = e.target.id.split('-');
    const resId = `${prefix}-${apiName}-${apiMethod}-result`;
    const td = document.getElementById(resId);
    const tokenElem = document.getElementById("token");
    const token = tokenElem ? tokenElem.innerText : null;
    if(td) {
        // Политика для Custom-Header:
        // - GET/POST отправляют заголовок (ожидаем успешный ответ),
        // - PUT/PATCH/DELETE не отправляют заголовок (ожидаем 403).
        // Это имитация "кнопки с заголовком" и "кнопки без заголовка".
        const shouldSendCustomHeader =
            apiName !== "order" || ["get", "post"].includes(apiMethod);

        const headers = {
            "Access-Control-Allow-Origin": "cgi221.loc",
            "Authorization": token == null || token.length == 0 
                ? "Basic YWRtaW46YWRtaW4=" 
                : `Bearer ${token}` // "admin" and password admin
        };

        if (apiName === "order" && shouldSendCustomHeader) {
            headers["Custom-Header"] = "My Value";
        }

        fetch(`/${apiName}`, {
            method: apiMethod.toUpperCase(),
            headers: headers
            
        }).then(r => {
            if (r.ok) {
                r.json().then(j => {
                    td.innerHTML = `<pre>${JSON.stringify(j, null, 4)}</pre>`;
                    if(j.meta.data_type == "token") {
                        document.getElementById("token").innerText = j.data;
                        const payloadBase64 = j.data.split('.')[1];
                        const payloadJson = JSON.parse(Base64.decodeUrl(payloadBase64));

                        document.getElementById("token-payload").innerHTML =
                            `<pre>${JSON.stringify(payloadJson, null, 4)}</pre>`;
                    }

                    const headerJson = Base64.jwtDecodeHeader(j.data);
                    document.getElementById("token-header").innerHTML =
                        `<pre>${JSON.stringify(headerJson, null, 4)}</pre>`;

                    // Учитываем успешный тест (ок-ответ).
                    registerTestResult(
                        `${apiName.toUpperCase()} ${apiMethod.toUpperCase()}`,
                        true
                    );
                });
            }
            else{
                r.text().then(t => {
                    td.innerText = t;
                    // Для order PUT/PATCH/DELETE отсутствие заголовка — ожидаемая ошибка.
                    const expectedOk = !(apiName === "order" && !shouldSendCustomHeader);
                    registerTestResult(
                        `${apiName.toUpperCase()} ${apiMethod.toUpperCase()}`,
                        !expectedOk
                    );
                });
            }
        })
    }
    else {
        throw "Container not found: " + resId;
    }
}

function runApiErrorTest(endpoint, method, headers, resId, expectedOk, title) {
    // Универсальная функция запуска теста "с ошибкой".
    // Все запросы выполняются одинаково, разница только в заголовках.
    const td = document.getElementById(resId);
    if (!td) {
        console.error("Container not found: " + resId);
        return;
    }

    td.innerHTML = "<i>Testing...</i>";

    fetch(endpoint, {
        method: method,
        headers: {
            "Access-Control-Allow-Origin": "cgi221.loc",
            ...headers 
        }
    })
    .then(r => r.json().catch(() => r.text()))
    .then(data => {
        if (typeof data === 'object') {
            td.innerHTML = `<pre style="color: #a00;">${JSON.stringify(data, null, 4)}</pre>`;
        } else {
            td.innerText = data;
        }
        // Обновляем итог по тестам.
        const testOk = expectedOk ? (data?.status?.isOk === true) : (data?.status?.isOk === false);
        registerTestResult(title, testOk);
    })
    .catch(err => {
        td.innerHTML = `<b style="color:red">Network Error:</b> ${err.message}`;
        registerTestResult(title, false);
    });
}

function registerTestResult(title, isOk) {
    // Сохраняем результат теста и обновляем сводку.
    // Важно: один запуск кнопки = один тест в статистике.
    testState.total += 1;
    if (isOk) {
        testState.passed += 1;
    } else {
        testState.failed += 1;
    }
    testState.details.push({ title, isOk });
    updateTestSummary();
}

function updateTestSummary() {
    // Формируем HTML итогов после каждого теста.
    // Это "общий результат", который нужен по ДЗ №12.
    const summary = document.getElementById("test-summary");
    if (!summary) {
        return;
    }
    const lines = testState.details.map(item => {
        const mark = item.isOk ? "✅" : "❌";
        return `<li>${mark} ${item.title}</li>`;
    }).join("");
    summary.innerHTML = `
        <p><b>Всього тестів:</b> ${testState.total}</p>
        <p><b>Успішних:</b> ${testState.passed}</p>
        <p><b>Неуспішних:</b> ${testState.failed}</p>
        <ul>${lines}</ul>
    `;
}
