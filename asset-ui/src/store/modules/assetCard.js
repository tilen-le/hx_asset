export default {
  state: {
    assetColumns: [],
    redirect: false,
  },
  mutations: {
    SET_ASSET_COLUMNS: (state, assetColumns) => {
      state.assetColumns = assetColumns;
    },
    SET_Redirect: (state, redirect) => {
      state.redirect = redirect;
    },
  },
};
