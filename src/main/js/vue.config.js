const {resolve} = require('path');
const CopyPlugin = require('copy-webpack-plugin');

module.exports = {
  outputDir: 'dist',
  chainWebpack: config => {
    config.entryPoints.delete('app');
    config.entry('config').add('./src/index.js');
    config.externals({
      vue: {
        commonjs: 'vue',
        commonjs2: 'vue',
        root: 'Vue'
      }
    });
    if (process.env.NODE_ENV === 'development') {
      //Fix different paths for watch-mode
      config.output.filename('js/[name].js');
      config.output.chunkFilename('js/[name].js');
    }
    config.output.libraryTarget('var');
    config.optimization.splitChunks(false);
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => ({
        ...options,
        hotReload: false
      }));
    config.plugins.delete('html');
    config.plugins.delete('preload');
    config.plugins.delete('prefetch');
  },
  configureWebpack: {
    plugins: [
      new CopyPlugin([{
        from: resolve(__dirname, 'src/routes.txt'),
        to: resolve(__dirname, 'dist'),
        toType: 'dir',
        ignore: ['*.scss']
      }])
    ]
  }
};
