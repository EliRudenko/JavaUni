document.addEventListener('DOMContentLoaded', () => {
    initApiTests();
});


class Base64 {
    static #textEncoder = new TextEncoder();
    static #textDecoder = new TextDecoder();

    // https://datatracker.ietf.org/doc/html/rfc4648#section-4
    static encode = (str) => btoa(String.fromCharCode(...Base64.#textEncoder.encode(str)));
    static decode = (str) => Base64.#textDecoder.decode(Uint8Array.from(atob(str), c => c.charCodeAt(0)));
    // https://datatracker.ietf.org/doc/html/rfc4648#section-5
    static encodeUrl = (str) => this.encode(str).replace(/\+/g, '-').replace(/\//g, '_'); //.replace(/=+$/, '');
    static decodeUrl = (str) => this.decode(str.replace(/\-/g, '+').replace(/\_/g, '/'));

    static jwtEncodeBody = (header, payload) => this.encodeUrl(JSON.stringify(header)) + '.' + this.encodeUrl(JSON.stringify(payload));
    static jwtDecodePayload = (jwt) => JSON.parse(this.decodeUrl(jwt.split('.')[1]));

    static jwtDecodeHeader = (jwt) => JSON.parse(this.decodeUrl(jwt.split('.')[0]));

}

function initApiTests() {
    const apiNames = ["user", "order", "discount"];
    const apiMethods = ["get", "post", "put", "patch", "delete"];
    for (let apiName of apiNames) {
        for (let apiMethod of apiMethods) {
            let btnId = `api-${apiName}-${apiMethod}-btn`;
            let btn = document.getElementById(btnId);
            if (btn) {
                btn.addEventListener('click', apiTestBtnClick);
            }
        }
    }
}


function apiTestBtnClick(e) {
    const [prefix, apiName, apiMethod, _] = e.target.id.split('-');
    const resId = `${prefix}-${apiName}-${apiMethod}-result`;
    const td = document.getElementById(resId);

    if (td) {
        const headers = {};
        if (apiName === "order") {
            headers["My-Custom-Header"] = "My Value";
        }

        fetch(`/${apiName}`, {
            method: apiMethod.toUpperCase(),
            headers: {
                "Access-Controll-Allow-Origin": "cgi221.loc",
                "My-Custom-Header": "My Value",
                "Authorization": "Basic YWRtaW46YWRtaW4="
            }
        }).then(r => {
            if (r.ok) {
                r.json().then(j => {
                    td.innerHTML = `<pre>${JSON.stringify(j, null, 4)}</pre>`;
                    document.getElementById("token").innerHTML = j.data;

                    const header = Base64.jwtDecodeHeader(j.data);
                    document.getElementById("token-header").innerHTML = `<b>Header:</b><pre>${JSON.stringify(header, null, 4)}</pre>`;

                    const payload = JSON.parse(Base64.decodeUrl(j.data.split('.')[1]));
                    document.getElementById("token-payload").innerHTML = `<pre>${JSON.stringify(payload, null, 4)}</pre>`;
                });

            } else {
                r.text().then(t => td.innerText = t);
            }
        });
    }
}
