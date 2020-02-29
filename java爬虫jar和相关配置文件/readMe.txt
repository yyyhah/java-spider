template 定义爬虫规则，设置解析器，存储器，元素筛选规则，Item保存对象

Item 存储页面元素信息的java类,需要实现Item

Atom Item的更小单元java类 需要实现Atom

ParseBean 解析器 可以自己编写 需要实现IParseBean，这里定义了三个基本解析器 htmlParseBean JsonParseBean BytesParseBean
分别解析html文本，json文本，字节流文本，前两种将需要的元素自动封装到Item中，BytesParseBean什么都不会做，直接返回字节流

SaveBean 存储器，自定义存储的方式，需要自己编写。需要实现ISaveBean。传入的参数未Item的实现类，这里需要注意如果Item
的类型为EmptyItem表示失败的请求，可以选择处理，如果是选择加入url再次请求，将url放入一个ConcurrentLinkedQueue<String>直接返回即可
如下
 if(entry.getValue() instanceof EmptyItem){
	urls.add(entry.getKey());
}
return urls；