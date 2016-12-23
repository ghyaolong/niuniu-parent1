module.exports = function(grunt) {
  //配置参数
  grunt.initConfig({
     pkg: grunt.file.readJSON('package.json'),
	 //压缩js
	uglify: {
         options: {
         },
         dist: {
             files: {
				 //压缩为一个文件main.js
                 'main.js': [
				            "../js/jquery.easing.1.3.js",
				            "../js/store.min.js",     
				            '../js/public.js',
				 ]
             }
         }
    },
	 //合并js
      concat: {
         options: {			 
             separator: ';',
             stripBanners: true
         },
         dist: {
			 //合并上一步压缩的文件与一个共公库
             src: [
			     "../js/jquery-1.11.0.min.js",				 
                 "../js/flickity.pkgd.min.js",
                 "main.js",					 
             ],
             dest: "../js/main.min.js"
         }
     }, 
     //合并css
     cssmin: {
         options: {
             keepSpecialComments: 0
         },
         compress: {
             files: {
                 '../css/main.min.css': [
				     "../css/normalize.css",
                     "../css/flickity.min.css",
                     "../css/css.css",		
                 ]
             }
         }
     },
	 //清除命令
	 clean: {
		all: ['main.js'],//清除压缩js时生成的临时文件main.js
		//image: 'dist/html/images',
		//css: 'dist/html/css',
		//html: 'dist/html/**/*'
	 },	 
	 //监控文件变化自动执行
	 watch: {
		  scripts: {
			files: ['../js/*.js'],
			tasks: ['uglify', 'concat','clean'],//js文件发生变化后执行
			options: {
			  spawn: true,
			  debounceDelay: 500,
			  event: ['changed', 'deleted'],//'all', 'changed', 'added' and 'deleted'.
			},
		  },
		  css: {
			files: ['../css/*.css'],
			tasks: ['cssmin'],//css文件发生变化后执行
			options: {
			  spawn: true,
			  debounceDelay: 500,
			  event: ['changed', 'deleted'],//'all', 'changed', 'added' and 'deleted'.
			},
		  },
	 },
  });

  //载入concat和uglify插件，分别对于合并和压缩
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');  
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-clean');
  
  //注册任务
  grunt.registerTask('default', ['uglify', 'concat',  'cssmin' ,'clean','watch']);

  
}