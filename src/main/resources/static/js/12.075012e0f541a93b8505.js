webpackJsonp([12],{"5XO3":function(t,e){},"GS+7":function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=i("Dd8w"),o=i.n(a),n=i("+cKO"),r=i("/QaM"),l=i("NYxO"),s={name:"Forgot",components:{EmailField:r.a},data:function(){return{email:""}},methods:o()({},Object(l.b)("profile/account",["passwordRecovery"]),{submitHandler:function(){var t=this;this.$v.$invalid?this.$v.$touch():this.passwordRecovery({email:this.email}).then(function(){t.$router.push({name:"ForgotSuccess"})})}}),validations:{email:{required:n.required,email:n.email}}},u={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"forgot"},[i("h2",{staticClass:"forgot__title form__title"},[t._v("Напишите ваш e‑mail")]),i("form",{staticClass:"forgot__form",on:{submit:function(e){return e.preventDefault(),t.submitHandler.apply(null,arguments)}}},[i("email-field",{attrs:{id:"forgot-email",v:t.$v.email},model:{value:t.email,callback:function(e){t.email=e},expression:"email"}}),i("div",{staticClass:"forgot__action"},[i("button-hover",{attrs:{tag:"button",type:"submit",variant:"white"}},[t._v("Отправить")])],1)],1)])},staticRenderFns:[]};var c=i("VU/8")(s,u,!1,function(t){i("5XO3")},null,null);e.default=c.exports}});
//# sourceMappingURL=12.075012e0f541a93b8505.js.map