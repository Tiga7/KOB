import { createStore } from "vuex";
import ModuleUser from "./user_op";

export default createStore({
	state: {},
	getters: {},
	mutations: {},
	actions: {},
	modules: {
		user: ModuleUser,
	},
});
