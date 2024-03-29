import $ from "jquery";

export default {
	state: {
		id: "",
		username: "",
		photo: "",
		token: "",
		is_login: false,
		pulling_info: true, //是否正在云端获取信息
	},
	getters: {},
	mutations: {
		//建议放一些同步操作
		//用mutations 里的函数 用store.commit
		updateUser(state, user) {
			state.id = user.id;
			state.username = user.username;
			state.photo = user.photo;
			state.is_login = user.is_login;
		},
		updateToken(state, token) {
			state.token = token;
		},
		logout(state) {
			state.id = "";
			state.username = "";
			state.photo = "";
			state.token = "";
			state.is_login = false;
		},
		upsdatePullingInfo(state, pulling_info) {
			state.pulling_info = pulling_info;
		},
	},
	actions: {
		//建议放一些异步操作
		//用actions 里的函数用store.dispatch
		login(context, data) {
			$.ajax({
				url: "http://localhost:3030/user/token/",
				type: "post",
				data: {
					username: data.username,
					password: data.password,
				},
				success(resp) {
					if (resp.message === "login_success") {
						//将token存储
						localStorage.setItem("jwt_token", resp.token);
						context.commit("updateToken", resp.token);
						data.success(resp);
					} else {
						data.error(resp);
					}
				},
				error(resp) {
					data.error(resp);
				},
			});
		},
		get_info(context, data) {
			$.ajax({
				url: "http://localhost:3030/user/getInfo/",
				type: "get",
				headers: {
					authorization: "Bearer " + context.state.token,
				},
				success(resp) {
					if (resp.message === "success") {
						context.commit("updateUser", {
							...resp,
							is_login: true,
						});
						data.success(resp);
					} else {
						data.error(resp);
					}
				},
				error(resp) {
					data.error(resp);
				},
			});
		},
		logout(context) {
			localStorage.removeItem("jwt_token");
			context.commit("logout");
		},
	},
	modules: {},
};
