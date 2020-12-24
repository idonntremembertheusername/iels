import jwtDecode from 'jwt-decode'

let Base64 = require('js-base64').Base64
let SIGN_REGEXP = /([yMdhsm])(\1*)/g
let DEFAULT_PATTERN = 'yyyy-MM-dd'

function padding(s, len) {
  len = len - (s + '').length
  for (var i = 0; i < len; i++) {
    s = '0' + s
  }
  return s
}


  function getActiveUser() {
    let uid = this.getCookie('uid')
    console.log('getActiveUser().get.uid= ' + uid)
    if (uid) {
      let activeUserStr = this.getUserSession('activeUser')
      console.log('getActiveUser: function ():::> ' + activeUserStr)
      return JSON.parse(activeUserStr)
    } else {
      this.delUserSession('activeUser')
    }
  }
  // 获取jwt令牌
   function getJwt() {
    let activeUser = this.getActiveUser()
    if (activeUser) {
      return activeUser.jwt
    }
  }
  // 解析jwt令牌，获取用户信息
   function getUserInfoFromJwt(jwt) {
    if (!jwt) {
      return
    }
    var jwtDecodeVal = jwtDecode(jwt)
    if (!jwtDecodeVal) {
      return
    }
    let activeUser = {}
    activeUser.utype = jwtDecodeVal.utype || ''
    activeUser.username = jwtDecodeVal.name || ''
    activeUser.userpic = jwtDecodeVal.userpic || ''
    activeUser.id = jwtDecodeVal.id || ''
    activeUser.authorities = jwtDecodeVal.authorities || ''
    activeUser.uid = jwtDecodeVal.jti || ''
    activeUser.jwt = jwt
    return activeUser
  }
   function checkActiveUser() {
    var jwtBase64 = this.getCookie('juid')
    if (jwtBase64) {
      let jwt = Base64.decode(jwtBase64)
      var jwtDecodeVal = jwtDecode(jwt)
      if (jwtDecodeVal) {
        let activeUser = {}
        activeUser.utype = jwtDecodeVal.utype || ''
        activeUser.username = jwtDecodeVal.userName || ''
        activeUser.userpic = jwtDecodeVal.userpic || ''
        activeUser.id = jwtDecodeVal.id || ''
        activeUser.authorities = jwtDecodeVal.authorities || ''
        activeUser.menus = jwtDecodeVal.menus || ''
        this.setSession('activeUser', JSON.stringify(activeUser))
        return this.getUserSession('activeUser')
      }
    }
    return null
  }
   function getCookie(name) {
    var reg = new RegExp('(^| )' + name + '=([^ ]*)( |$)')
    var arr = document.cookie.match(reg)
    if (arr) {
      return (arr[2])
    } else {
      return null
    }
  }
  function setCookie(cName, value, expiredays) {
    var exdate = new Date()
    exdate.setDate(exdate.getDate() + expiredays)
    document.cookie = cName + '=' + escape(value) + ((expiredays == null) ? '' : ' expires=' + exdate.toGMTString())
  }
  function delCookie(name) {
    var exp = new Date()
    exp.setTime(exp.getTime() - 1)
    document.cookie = name + '=' + ' expires=' + exp.toGMTString()
  }
  function setSession(key, value) {
    return sessionStorage.setItem(key, value)
  }
  function getUserSession(key) {
    return sessionStorage.getItem(key)
  }
  function setUserSession(key, value) {
    return sessionStorage.setItem(key, value)
  }
  function setUserSession(key) {
    return sessionStorage.removeItem(key)
  }
  function getQueryStringByName(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i')
    var r = window.location.search.substr(1).match(reg)
    var context = ''
    if (r != null) {
      context = r[2]
    }
    reg = null
    r = null
    return context == null || context === '' || context === 'undefined' ? '' : context
  }
  function formatDate(date, pattern) {
    pattern = pattern || DEFAULT_PATTERN
    return pattern.replace(SIGN_REGEXP, function ($0) {
      switch ($0.charAt(0)) {
        case 'y':
          return padding(date.getFullYear(), $0.length)
        case 'M':
          return padding(date.getMonth() + 1, $0.length)
        case 'd':
          return padding(date.getDate(), $0.length)
        case 'w':
          return date.getDay() + 1
        case 'h':
          return padding(date.getHours(), $0.length)
        case 'm':
          return padding(date.getMinutes(), $0.length)
        case 's':
          return padding(date.getSeconds(), $0.length)
      }
    })
  }
  function parseDate(dateString, pattern) {
    var matchs1 = pattern.match(SIGN_REGEXP)
    var matchs2 = dateString.match(/(\d)+/g)
    if (matchs1.length === matchs2.length) {
      var _date = new Date(1970, 0, 1)
      for (var i = 0; i < matchs1.length; i++) {
        var _int = parseInt(matchs2[i])
        var sign = matchs1[i]
        switch (sign.charAt(0)) {
          case 'y':
            _date.setFullYear(_int)
            break
          case 'M':
            _date.setMonth(_int - 1)
            break
          case 'd':
            _date.setDate(_int)
            break
          case 'h':
            _date.setHours(_int)
            break
          case 'm':
            _date.setMinutes(_int)
            break
          case 's':
            _date.setSeconds(_int)
            break
        }
      }
      return _date
    }
    return null
  }

module.exports = {
  getActiveUser : getActiveUser,
  getCookie :getCookie,
  getJwt : getJwt,
  getUserInfoFromJwt : getUserInfoFromJwt,
  checkActiveUser:checkActiveUser,
  setCookie:setCookie,
  parseDate:parseDate,
  formatDate:formatDate,
  setSession:setSession
}
