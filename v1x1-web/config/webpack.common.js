var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var helpers = require('./helpers');

module.exports = {
  entry: {
    'app': './app/main.ts'
  },

  resolve: {
    extensions: ['.ts', '.js'],
    alias: {
      '@fortawesome/fontawesome-free-regular$': '@fortawesome/fontawesome-free-regular/shakable.es.js',
      '@fortawesome/fontawesome-free-brands$': '@fortawesome/fontawesome-free-brands/shakable.es.js',
      '@fortawesome/fontawesome-pro-regular$': '@fortawesome/fontawesome-pro-regular/shakable.es.js'
    }
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
        test: /\.(png|jpe?g|gif|svg|ico)$/,
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
      },
      {
        test: /\.woff(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader",
        options: {
          // Limit at 50k. Above that it emits separate files
          limit: 50000,

          // url-loader sets mimetype if it's passed.
          // Without this it derives it from the file extension
          mimetype: "application/font-woff",

          // Output below fonts directory
          name: "./fonts/[name].[ext]"
        }
      },
      {
        test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader",
        options: {
          limit: 50000,
          mimetype: "font/woff",
          name: "./fonts/[name].[ext]"
        }
      },
      {
        test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader",
        options: {
          limit: 50000,
          mimetype: "application/vnd.ms-fontobject",
          name: "./fonts/[name].[ext]"
        }
      },
      {
        test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader",
        options: {
          limit: 50000,
          mimetype: "application/font-sfnt",
          name: "./fonts/[name].[ext]"
        }
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
