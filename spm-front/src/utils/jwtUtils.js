//jwtUtils.js
//用于jwt相关操作，包括解析等

function decodeBase64Url(base64Url) {
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(atob(base64));
}

export function decodeJwt(token) {
    const parts = token.split('.')
    
    const header = decodeBase64Url(parts[0]);
    const payload = decodeBase64Url(parts[1]);

    return [header, payload]
}