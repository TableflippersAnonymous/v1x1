var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var helpers = require('./helpers');

module.exports = {
  entry: {
    'app': './app/main.ts'
  },

  resolve: {
    extensions: ['.ts', '.js']
  },

  module: {
    rules: [
      {
        test: /\.ts$/,
        loaders: ['awesome-typescript-loader', 'angular2-template-loader']
      },
      {
        test: /\.html$/,
        loader: 'html-loader'
      },
      {
        test: /\.(png|jpe?g|gif|svg|ico|woff|woff2|eot|ttf)$/,
        loader: 'file-loader?name=assets/[name].[hash].[ext]'
      },
      /*{
        test: /\.css$/,
        exclude: helpers.root('app'),
        loader: ExtractTextPlugin.extract('style', 'css?sourceMap')
      },
      {
        test: /\.css$/,
        include: helpers.root('app'),
        loader: 'raw'
      },*/
      {
        test: /\.scss$/,
        loader: ExtractTextPlugin.extract({
          fallback: "style-loader?sourceMap",
          use: ["css-loader?sourceMap", "sass-loader?sourceMap"]
        })
      }
    ]
  },

  plugins: [
    new webpack.ContextReplacementPlugin(
      // The (\\|\/) piece accounts for path separators in *nix and Windows
      /angular(\\|\/)core(\\|\/)(esm(\\|\/)src|src)(\\|\/)linker/,
      helpers.root('app'), // location of your src
      { }
    ),
    new HtmlWebpackPlugin({
      template: 'index.html'
    })
  ],

  optimization: {
    splitChunks: {
      cacheGroups: {
        vendor: {
          test: /node_modules|vendor/,
          name: 'vendor',
          chunks: 'all'
        }
      }
    }
  }

};
