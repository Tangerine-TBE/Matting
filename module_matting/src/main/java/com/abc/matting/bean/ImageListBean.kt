package com.abc.matting.bean

class ImageListBean {
    private var code = 0
    private var msg = ""
    private var data: Array<ImageListData> = arrayOf()

    class ImageListData{
        private var image_id = 0
        private var url = ""
        private var kind = ""
        private var category = ""
        private var view_count = 0
        private var create_time = ""
    }
}