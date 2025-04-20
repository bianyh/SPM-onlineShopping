//imgio.js
import myAxios from "@/config/axios";

//图像上传
export function uploadImage(file,usage = "product", itemId = "1", imgId = 0) {
    var form_data = new FormData()
    form_data.append('file',file)
    return myAxios({
        url: '/api/image/upload',
        method: 'post',
        params: {
            usage: usage,
            itemId: itemId,
            imgId: imgId
        },
        data: form_data,
        headers: {
            "Content-Type": 'multipart/form-data'
        }
    })
}

export function getImageUrl(resource) {
    return '127.0.0.1:8080/' + resource
}