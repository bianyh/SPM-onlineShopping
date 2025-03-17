//imgio.js
import myAxios from "@/config/axios";

//图像上传
export function uploadImage(file) {
    var form_data = new FormData()
    form_data.append('image',file)
    return myAxios({
        url: '/api/upload',
        method: 'post',
        params: form_data,
        headers: {
            "Content-Type": 'multipart/form-data'
        }
    })
}

export function getImageUrl(resource) {
    return '127.0.0.1:8080/' + resource
}