import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/Home/Home';
import Login from '../components/Login/Login.vue';
import Purchase from "../components/ShopCart/Purchase";
import Personal from '../components/Personnal/Personal.vue';


Vue.use(Router)

const router = new Router({
  linkActiveClass: 'is-active',
  mode: 'history',
  routes: [{
    path: '/',
    redirect: '/home'
  },
    {
      path: '/home',
      name: 'Home',
      component: Home
    },
    {
      path: '/purchase',
      name: 'Purchase',
      component: Purchase
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/logintest',
      name: 'Logintest',
      component: Login
    },

    {
      path: '/personpage',
      name: 'Personal',
      component: Personal,
      meta: {
        requireAuth: true,
      }
    },
  ]
})
export default router
