// store
import { createStore } from 'vuex';

export default createStore({
  state: {
    sharedData: null
  },
  mutations: {
    setSharedData(state, data) {
      state.sharedData = data;
    }
  }
});