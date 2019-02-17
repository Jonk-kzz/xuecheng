var {add} = require('./model01')
var Vue = require('../../js/vue/vue.min')
var VM =new Vue({
    el: "#app",//表示当前vue对象接管app的div区域
    data:{
        name:'幻方科技有限公司', // 相当于是MVVM中的Model这个角色
        num1:0,
        num2:0,
        result:0,
        url:'http://steven.org.cn:8081/user/login'
    },
    methods:{
        change:function () {
            //这里使用了导入的model01.js文件中的add方法
            this.result=add(Number.parseInt(this.num1),Number.parseInt(this.num2))
            debugger
            alert(this.result)

        }
    }
});