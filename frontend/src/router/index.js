import { createRouter, createWebHistory } from 'vue-router'

import index from "@/index.vue";
import content from "@/content.vue";
import modify from "@/modify.vue";
import write from "@/write.vue";
import deleteContent from "@/delete.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: index
    },
    {
      path: '/index',
      name: 'index',
      component: index
    },
    {
      path: '/content?boardNum=:boardNum',
      name: 'content',
      props: true,
      component: content
    },
    {
      path: '/modify',
      name: 'modify',
      props: true,
      component: modify
    },
    {
      path: '/write',
      name: 'write',
      component: write
    },
    {
      path: '/delete?boardNum=:boardNum',
      name: 'delete',
      props: true,
      component: deleteContent
    }
  ]
})

export default router
