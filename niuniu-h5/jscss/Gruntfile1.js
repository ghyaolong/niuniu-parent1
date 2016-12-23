module.exports = function (grunt) {

    // ������������
    grunt.initConfig({

        //��ȡpackage.json�����ݣ��γɸ�json����
        pkg: grunt.file.readJSON('package.json'),

        //ѹ��js
        uglify: {
            //�ļ�ͷ�������Ϣ
            options: {
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            my_target: {
                files: [
                    {
                        expand: true,
                        //���·��
                        cwd: 'js/',
                        src: '*.js',
                       //src: ['**/*.js', '!**/*.min.js'],  //������ĳ��js,ĳ���ļ����µ�js
                        dest: 'dest/js/',
                        rename: function (dest, src) {  
                                  var folder = src.substring(0, src.lastIndexOf('/'));  
                                  var filename = src.substring(src.lastIndexOf('/'), src.length);  
                                  //  var filename=src;  
                                  filename = filename.substring(0, filename.lastIndexOf('.'));  
                                  var fileresult=dest + folder + filename + '.min.js';  
                                  grunt.log.writeln("�ִ����ļ���"+src+"  ������ļ���"+fileresult);  

                                  return fileresult;  
                                  //return  filename + '.min.js';  
                              } 
                    }
                ]
            }
        },

        //ѹ��css
        cssmin: {
            //�ļ�ͷ�������Ϣ
            options: {
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n',
                //��������
                beautify: {
                    //����ascii�����ǳ����ã���ֹ���������������
                    ascii_only: true
                }
            },
            my_target: {
                files: [
                    {
                        expand: true,
                        //���·��
                        cwd: 'css/',
                        src: '*.css',
                        dest: 'dest/css/',
                        rename: function (dest, src) {  
                                var folder = src.substring(0, src.lastIndexOf('/'));  
                                var filename = src.substring(src.lastIndexOf('/'), src.length);  
                                //  var filename=src;  
                                filename = filename.substring(0, filename.lastIndexOf('.'));  
                                var fileresult=dest + folder + filename + '.min.css';  
                                grunt.log.writeln("�ִ����ļ���"+src+"  ������ļ���"+fileresult);  

                                return fileresult;  
                              //return  filename + '.min.js';
                                }
                    }
                ]
            }
        }

    });

    // ����ָ���������
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-cssmin');

    // Ĭ��ִ�е�����
    grunt.registerTask('default', ['uglify', 'cssmin']);

};