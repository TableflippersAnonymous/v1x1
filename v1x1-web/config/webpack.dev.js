var webpackMerge = require('webpack-merge');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var commonConfig = require('./webpack.common.js');
var helpers = require('./helpers');

module.exports = webpackMerge(commonConfig, {
  devtool: 'cheap-module-eval-source-map',

  output: {
    path: helpers.root('dist'),
    publicPath: '/',
    filename: '[name].js',
    chunkFilename: '[id].chunk.js'
  },

  plugins: [
    new ExtractTextPlugin({
      filename: '[name].css',
      allChunks: true
    })
  ],

  devServer: {
    disableHostCheck: true,
    historyApiFallback: true,
    stats: 'minimal',
    proxy: {
      '/api': {
        target: 'http://nano.fox.ninja:8080',
        secure: false,
        changeOrigin: true
      }
    }
  },

  mode: 'development'

});
