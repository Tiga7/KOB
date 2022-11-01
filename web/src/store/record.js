export default {
	state: {
		is_record: false,
		records: "",
		a_steps: "",
		b_steps: "",
	},
	getters: {},
	mutations: {
		updateIsRecord(state, is_record) {
			state.is_record = is_record;
		},
		updateSteps(state, data) {
			state.a_steps = data.a_steps;
			state.b_steps = data.b_steps;
		},
	},
	actions: {},
	modules: {},
};
