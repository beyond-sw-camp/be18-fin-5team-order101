import { createRouter, createWebHistory } from 'vue-router'

const DashboardView = () => import('../views/DashboardView.vue')
const PagePlaceholder = () => import('../views/PagePlaceholder.vue')
const UserRegistrationView = () => import('../views/hq/user/UserRegistrationView.vue')
const UserListView = () => import('../views/hq/user/UsersListView.vue')
const StoreInventoryView = () => import('../views/store/inventory/InventoryView.vue')
const LoginView = () => import('../views/LoginView.vue')

const hqRoutes = [
  {
    path: '/hq/dashboard',
    name: 'hq-dashboard',
    component: DashboardView,
    meta: { title: 'HQ 대시보드' },
  },
  {
    path: '/hq/orders/create',
    name: 'hq-orders-create',
    component: PagePlaceholder,
    meta: { title: '구매 주문 생성' },
  },
  {
    path: '/hq/orders/status',
    name: 'hq-orders-status',
    component: PagePlaceholder,
    meta: { title: '주문 현황' },
  },
  {
    path: '/hq/orders/approval',
    name: 'hq-orders-approval',
    component: PagePlaceholder,
    meta: { title: '주문 승인' },
  },
  {
    path: '/hq/orders/vendors',
    name: 'hq-orders-vendors',
    component: PagePlaceholder,
    meta: { title: '공급사 관리' },
  },
  {
    path: '/hq/inventory/stock',
    name: 'hq-inventory-stock',
    component: PagePlaceholder,
    meta: { title: '재고 상태' },
  },
  {
    path: '/hq/inventory/movements',
    name: 'hq-inventory-movements',
    component: PagePlaceholder,
    meta: { title: '입출고 조회' },
  },
  {
    path: '/hq/settlement/daily',
    name: 'hq-settlement-daily',
    component: PagePlaceholder,
    meta: { title: '일일 정산' },
  },
  {
    path: '/hq/users/',
    name: 'hq-users-list',
    component: UserListView,
    meta: { title: '사용자 목록' },
  },
  {
    path: '/hq/users/registration',
    name: 'hq-users-registration',
    component: UserRegistrationView,
    meta: { title: '사용자 등록' },
  },

  {
    path: '/hq/franchise/registration',
    name: 'hq-franchise-registration',
    component: PagePlaceholder,
    meta: { title: '가맹점 등록' },
  },
  {
    path: '/hq/franchise/stock',
    name: 'hq-franchise-stock',
    component: PagePlaceholder,
    meta: { title: '가맹점 재고 현황' },
  },
  {
    path: '/hq/franchise/approval',
    name: 'hq-franchise-approval',
    component: PagePlaceholder,
    meta: { title: '가맹점 주문 승인' },
  },
  {
    path: '/hq/franchise/orders',
    name: 'hq-franchise-orders',
    component: PagePlaceholder,
    meta: { title: '가맹점 주문 조회' },
  },
  {
    path: '/hq/franchise/delivery',
    name: 'hq-franchise-delivery',
    component: PagePlaceholder,
    meta: { title: '배송 관리' },
  },
]

const storeRoutes = [
  {
    path: '/store/dashboard',
    name: 'store-dashboard',
    component: PagePlaceholder,
    meta: { title: '스토어 대시보드' },
  },
  {
    path: '/store/purchase/create',
    name: 'store-purchase-create',
    component: PagePlaceholder,
    meta: { title: '발주 생성' },
  },
  {
    path: '/store/inventory/stock',
    name: 'store-inventory-stock',
    component: StoreInventoryView,
    meta: { title: '재고 조회' },
  },
  {
    path: '/store/settlement/overview',
    name: 'store-settlement-overview',
    component: PagePlaceholder,
    meta: { title: '정산 관리' },
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'dashboard', component: DashboardView, meta: { title: '대시보드' } },
    { path: '/login', name: 'login', component: LoginView, meta: { title: '로그인' } },
    ...hqRoutes,
    ...storeRoutes,
    // { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

export default router
