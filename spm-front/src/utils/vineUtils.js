//vineUtils.js

// 用于通过接口传输数据

export function transParams(params) {
    let result = ''
    if (params == undefined) return result;
    for (const propName of Object.keys(params)){
        const value = params[propName];
        var part = encodeURIComponent(propName) + "=";
        if (value !== null && typeof (value) !== "undefined") {
            if (typeof value === 'object') {
                for (const key of Object.keys(value)) {
                    if (value[key] !== null && typeof (value[key]) !== "undefined"){
                        let params = propName + '[' + key + ']';
                        var subPart = encodeURIComponent(params) + '=';
                        result += subPart + encodeURIComponent(value[key]) + '&';
                    }
                }
            } else {
            result += part + encodeURIComponent(value) + "&";
            }
        }
    }
    return result
}