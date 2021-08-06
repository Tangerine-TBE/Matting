package com.abc.matting.bean

class ImageCategoryBean {
    private var code = 0
    private var msg = ""
    private var data:Array<ImageCategoryData> = arrayOf()

    class ImageCategoryData{
        private var id = 0
        private var kind = ""
        private var describe = ""
        private var quantity = 0
    }
}