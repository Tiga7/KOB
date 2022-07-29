import { createRouter, createWebHistory } from "vue-router";

import NotFound from "../views/error/NotFound.vue";
import PkView from "../views/pk/PkView.vue";
import RankList from "../views/ranklist/RankListView.vue";
import RecordView from "../views/record/RecordView.vue";
import UserBot from "../views/user/bot/UserBotView.vue";

const routes = [
	{
		path: "/",
		name: "home",
		redirect: "/pk/",
	},
	{
		path: "/pk/",
		name: "pk_index",
		component: PkView,
	},
	{
		path: "/ranklist/",
		name: "ranklist_index",
		component: RankList,
	},
	{
		path: "/record/",
		name: "record_index",
		component: RecordView,
	},
	{
		path: "/user/bot/",
		name: "user_bot",
		component: UserBot,
	},
	{
		path: "/404/",
		name: "not_found",
		component: NotFound,
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

export default router;