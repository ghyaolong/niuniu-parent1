module.exports = function(grunt) {
  //���ò���
  grunt.initConfig({
     pkg: grunt.file.readJSON('package.json'),
	 //ѹ��js
	uglify: {
         options: {
         },
         dist: {
             files: {
				 //ѹ��Ϊһ���ļ�main.js
                 'main.js': [
				            "../js/jquery.easing.1.3.js",
				            "../js/store.min.js",     
				            '../js/public.js',
				 ]
             }
         }
    },
	 //�ϲ�js
      concat: {
         options: {			 
             separator: ';',
             stripBanners: true
         },
         dist: {
			 //�ϲ���һ��ѹ�����ļ���һ��������
             src: [
			     "../js/jquery-1.11.0.min.js",				 
                 "../js/flickity.pkgd.min.js",
                 "main.js",					 
             ],
             dest: "../js/main.min.js"
         }
     }, 
     //�ϲ�css
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
	 //�������
	 clean: {
		all: ['main.js'],//���ѹ��jsʱ���ɵ���ʱ�ļ�main.js
		//image: 'dist/html/images',
		//css: 'dist/html/css',
		//html: 'dist/html/**/*'
	 },	 
	 //����ļ��仯�Զ�ִ��
	 watch: {
		  scripts: {
			files: ['../js/*.js'],
			tasks: ['uglify', 'concat','clean'],//js�ļ������仯��ִ��
			options: {
			  spawn: true,
			  debounceDelay: 500,
			  event: ['changed', 'deleted'],//'all', 'changed', 'added' and 'deleted'.
			},
		  },
		  css: {
			files: ['../css/*.css'],
			tasks: ['cssmin'],//css�ļ������仯��ִ��
			options: {
			  spawn: true,
			  debounceDelay: 500,
			  event: ['changed', 'deleted'],//'all', 'changed', 'added' and 'deleted'.
			},
		  },
	 },
  });

  //����concat��uglify������ֱ���ںϲ���ѹ��
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');  
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-clean');
  
  //ע������
  grunt.registerTask('default', ['uglify', 'concat',  'cssmin' ,'clean','watch']);

  
}