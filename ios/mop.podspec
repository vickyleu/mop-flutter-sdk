#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'mop'
  s.version          = '0.1.1'
  s.summary          = 'finclip miniprogram flutter sdk'
  s.description      = <<-DESC
A finclip miniprogram flutter sdk.
                       DESC
  s.homepage         = 'https://www.finclip.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'finogeeks' => 'contact@finogeeks.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.ios.deployment_target = '11.0'

  s.dependency 'FinApplet' , '2.43.11-fix20240620v01'
  s.dependency 'FinAppletExt' , '2.43.11-fix20240620v01'
end

