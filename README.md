SnailPhotoCache
===============

采用 LruCache  DiskLruCache 图片本地缓存

参考 https://github.com/fhucho/simple-disk-cache 
     https://github.com/JakeWharton/DiskLruCache

先从LruCache中读取，获取不成功，则从DiskLruCache中获取

![SnailPhotoCache](https://github.com/wangdaliu/SnailPhotoCache/blob/master/demo.jpg?raw=true)
