import Vue from 'vue'
import Vuex from 'vuex'

// 导入模块
import app from './modules/app'
import user from './modules/user'
import timer from './modules/timer'
import playlist from './modules/playlist'

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
		user,
		timer,
		playlist
	}
})

export default store 