template 定义爬虫规则，设置解析器，存储器，元素筛选规则，Item保存对象

Item 存储页面元素信息的java类,需要实现Item

ParseBean 解析器 可以自己编写 需要实现IParseBean，这里定义了三个基本解析器 htmlParseBean JsonParseBean BytesParseBean
分别解析html文本，json文本，字节流文本，前两种将需要的元素自动封装到Item中，BytesParseBean什么都不会做，直接返回字节流。自己实现必须实现带Template的构造器。

SaveBean 存储器，自定义存储的方式，需要自己编写。需要实现ISaveBean，必须有空参的构造器。传入的参数未Item的实现类，这里需要注意如果Item
的类型为EmptyItem表示失败的请求，可以选择处理，如果是选择加入url再次请求，将url放入一个ConcurrentLinkedQueue<String>直接返回即可
如下
 if(entry.getValue() instanceof EmptyItem){
	urls.add(entry.getKey());
}
return urls；

启动方法
private List<Template> getTemplate(){
    ArrayList<Template> lists = new ArrayList<>();
    //创建模板
    Template template = new Template();
    template.setCharset("utf-8");
    template.setUrlReg("https://api.bilibili.com/x/web-interface/ranking/region\\?rid=33&day=3&original=0");
    HashMap<String, String> temp = new HashMap<>();
    temp.put("atoms","data");
    temp.put("atoms.typename","typename");
    temp.put("atoms.title","title");
    temp.put("atoms.pic","pic");
    template.setElementPath(temp);
    template.setParseBean(JsonParseBean.class.getName());
    template.setItem("com.itcast.item.BilibiliItem");
    template.setSaveBean("com.itcast.save.DBSaveBean4");
    lists.add(template);


    Template template1 = new Template();
    template1.setCharset("utf-8");
    template1.setUrlReg("http://i\\d.hdslb.com/bfs/archive/.*?.jpg");
    template1.setParseBean(ByteParseBean.class.getName());
    template1.setItem(ByteItem.class.getName());
    template1.setSaveBean("com.itcast.save.DBSaveBean3");
    lists.add(template1);
    return lists;
}
    @Test
    public void testStartOneRequest() throws ClassNotFoundException {
        //加载匹配
        Class.forName("com.wsf.config.Configure");
        //如果资源池中没有资源可以通过读取器写入资源
	//WriteToUrl write = new WriteToUrl();
        //写入初始网址
        //ConcurrentLinkedQueue<String> inBuffer = new ConcurrentLinkedQueue<>();
        inBuffer.add("https://api.bilibili.com/x/web-interface/ranking/region?rid=33&day=3&original=0");
        //write.write(inBuffer);
        CenterControllerImpl center = new CenterControllerImpl(getTemplate());
        center.start();
        center.destroy();
    }
}
