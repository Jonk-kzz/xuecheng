//public是对axios的工具类封装,定义了HTTP请求方法
import http from './../../../base/api/public'
import querystring from 'querystring'
let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.xcApiUrlPre;

export const page_list=(page,size,params)=>{

  let query =querystring.stringify(params)
  return http.requestQuickGet(apiUrl+'/cms/page/list/'+page+'/'+size+'/?'+query)
}

export const find_site=()=>{
  return http.requestQuickGet(apiUrl+'/cms/page/site')
}

export const  find_template=()=>{
  return http.requestQuickGet(apiUrl+'/cms/page/template')
}

export const page_add=(pageForm)=>{
  return http.requestPost(apiUrl+'/cms/page/add',pageForm)
}

export const page_get = id => {
  return http.requestQuickGet(apiUrl+'/cms/page/get/'+id)
}

export const page_edit=(id,pageEdit)=>{
  return http.requestPut(apiUrl+'/cms/page/edit/'+id,pageEdit)
}

export const page_del=(id)=>{
  return http.requestDelete(apiUrl+'/cms/page/del/'+id)
}


