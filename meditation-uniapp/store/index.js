import Vue from 'vue'
import Vuex from 'vuex'

// 导入模块
import app from './modules/app'
import user from './modules/user'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		// 全局状态
	},
	mutations: {
		// 全局mutations
	},
	actions: {
		// 全局actions
	},
	modules: {
		app,
		user
	}
})

export default store 