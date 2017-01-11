'use strict';

// Declare app level module which depends on views, and components
angular.module('v1x1', [
  'ngRoute',
  'v1x1.view1',
  'v1x1.view2',
  'v1x1.version'
]).
config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
  $locationProvider.hashPrefix('!');

  $routeProvider.otherwise({redirectTo: '/view1'});
}]);
