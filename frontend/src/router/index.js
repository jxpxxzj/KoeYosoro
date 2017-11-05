import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Train from '@/components/Train'
import Pinyin from '@/components/Pinyin'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: HelloWorld,
      meta: {
        title: 'Speech'
      }
    },
    {
      path: '/train',
      name: 'Train',
      component: Train, 
      meta: {
        title: 'Train'
      }
    },
    {
      path: '/pinyin',
      name: 'Pinyin',
      component: Pinyin,
      meta: {
        title: 'Pinyin'
      }
    }
  ]
});
