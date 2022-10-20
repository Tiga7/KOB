import { createRouter, createWebHistory } from "vue-router";

import NotFound from "../views/error/NotFound.vue";
import PkView from "../views/pk/PkView.vue";
import RankList from "../views/ranklist/RankListView.vue";
import RecordView from "../views/record/RecordView.vue";
import UserBot from "../views/user/bot/UserBotView.vue";

import UserLoginView from "../views/user/account/UserLoginView.vue";
import UserRegisterView from "../views/user/account/UserRegisterView.vue";
import HomeView from "../views/HomeView.vue";
import store from "../store/index";

const routes = [
	{
		path: "/",
		name: "home",
		component: HomeView,
		meta: {
			//授权控制
			requestAuth: false,
		},
	},
	{
		path: "/home",
		name: "home",
		component: HomeView,
		meta: {
			requestAuth: false,
		},
	},
	{
		path: "/pk/",
		name: "pk_index",
		component: PkView,
		meta: {
			requestAuth: true,
		},
	},
	{
		path: "/ranklist/",
		name: "ranklist_index",
		component: RankList,
		meta: {
			requestAuth: true,
		},
	},
	{
		path: "/record/",
		name: "record_index",
		component: RecordView,
		meta: {
			requestAuth: true,
		},
	},
	{
		path: "/user/bot/",
		name: "user_bot",
		component: UserBot,
		meta: {
			requestAuth: true,
		},
	},
	{
		path: "/user/login/",
		name: "user_login",
		component: UserLoginView,
		meta: {
			requestAuth: false,
		},
	},
	{
		path: "/user/register/",
		name: "user_register",
		component: UserRegisterView,
		meta: {
			requestAuth: false,
		},
	},
	{
		path: "/404/",
		name: "404",
		component: NotFound,
		meta: {
			requestAuth: false,
		},
	},
	{
		path: "/:catchAll(.*)",
		redirect: "/404/",
	},
];

const router = createRouter({
	history: createWebHistory(),
	routes,
});

router.beforeEach((to, from, next) => {
	if (to.meta.requestAuth && !store.state.user.is_login) {
		next({ name: "user_login" });
	} else {
		next();
	}
});

export default router;
