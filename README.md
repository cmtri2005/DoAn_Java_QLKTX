
# Quản lý ký túc xá - 4T Dormitory

## Lời cảm ơn
Lời nói đầu, cho phép nhóm em gửi lời cảm ơn đến thầy/cô đã giúp tụi em có những kiến thức nền tảng của ngôn ngữ lập trình Java và hỗ trợ tận tình những thiếu sót để nhóm em có thể hoàn thiện được đồ án môn học.

## Chỉnh sửa
Đã decorate màu sắc một số trang như form thanh toán, đăng ký dịch vụ, ...

## Nhóm 4T Dormitory
| MSSV     | Họ tên             | Đánh giá % |
|----------|--------------------|------------|
| 23521635 | Cao Minh Trí       | 25         |
| 23521643 | Nguyễn Minh Trí    | 25         |
| 23521593 | Nguyễn Thanh Tín   | 25         |
| 23521528 | Nguyễn Ngọc Thông  | 25         |

## Giới thiệu đồ án
- Hiện nay, cùng với sự phát triển không ngừng của xã hội, số lượng sinh viên theo học tại các trường đại học, cao đẳng trên cả nước ngày càng gia tăng. Điều này kéo theo nhu cầu về chỗ ở trong ký túc xá cũng tăng cao, đòi hỏi công tác quản lý phải ngày càng chuyên nghiệp, hiệu quả và hiện đại hơn. Tuy nhiên, trên thực tế, nhiều ký túc xá vẫn đang vận hành theo phương pháp quản lý thủ công hoặc bán thủ công, gây ra nhiều khó khăn trong việc theo dõi thông tin sinh viên, phân bổ phòng ở, quản lý dịch vụ và giám sát hoạt động sinh hoạt hàng ngày.
- Đặc biệt, khi số lượng sinh viên lớn, tần suất đăng ký, chuyển phòng, hoặc nghỉ lưu trú thay đổi thường xuyên, thì việc quản lý bằng phương pháp truyền thống sẽ trở nên quá tải, dễ xảy ra sai sót và thiếu minh bạch. Điều này không chỉ ảnh hưởng đến hiệu suất làm việc của bộ phận quản lý mà còn tác động tiêu cực đến trải nghiệm của sinh viên trong quá trình lưu trú tại ký túc xá.Chính vì vậy, để đáp ứng yêu cầu quản lý một cách khoa học, hiện đại và phù hợp với thực tiễn, việc xây dựng một hệ thống phần mềm quản lý ký túc xá là hết sức cần thiết. Hệ thống này sẽ hỗ trợ ban quản lý trong việc kiểm soát thông tin sinh viên, tình trạng phòng ở, nhân sự quản lý, dịch vụ đi kèm cũng như tổng hợp, báo cáo tình hình vận hành theo chu kỳ. Đây cũng là nền tảng quan trọng để các cơ sở đào tạo nâng cao chất lượng dịch vụ nội trú và hướng tới mục tiêu quản lý số hóa toàn diện trong tương lai.
- Trước thực trạng đó, nhóm đề xuất xây dựng một hệ thống quản lý ký túc xá nhằm hỗ trợ số hóa và tin học hóa toàn bộ quy trình vận hành, giúp nâng cao hiệu quả công việc, giảm thiểu sai sót, tiết kiệm thời gian và tăng tính minh bạch trong quản lý.
-- Hệ thống được thiết kế với hai nhóm người dùng chính là sinh viên và quản lý (bao gồm nhân viên và quản trị viên hệ thống), với các phân quyền và chức năng phù hợp cho từng đối tượng. Với sinh viên - người sử dụng hệ thống với quyền hạn giới hạn. Sinh viên có thể thực hiện các chức năng như đăng ký và đăng nhập tài khoản, đăng ký phòng ở theo theo khu vực, loại phòng và tình trạng chỗ trống. Ngoài ra, hệ thống còn cung cấp chức năng tra cứu thông tin phòng đang ở, lịch sử lưu trú và các khoản phí khác. Một số chức năng khác dành cho sinh viên như chuyển phòng, gia hạn phòng, nhận thông báo, yêu cầu hỗ trợ. Đối với quản lý - người thuộc bộ phận quản trị. Quản lý cũng có các chức năng đăng nhập, tra cứu như sinh viên, chức năng chính của họ là quản lí hệ thống như quản lý thông tin sinh viên, nhân sư, phòng ở, dịch vụ. Bên cạnh đó, họ còn quản lý việc xử lí yêu cầu đăng ký, thanh toán và tạo báo cáo thống kê.
-- Với cấu trúc đơn giản nhưng hiệu quả, hệ thống không chỉ đáp ứng các yêu cầu nghiệp vụ cơ bản mà còn có khả năng mở rộng và nâng cấp trong tương lai. Đây là một giải pháp phù hợp với thực tiễn, góp phần hiện đại hóa công tác quản lý ký túc xá, đồng thời nâng cao trải nghiệm cho sinh viên và hỗ trợ đắc lực cho đội ngũ cán bộ quản lý.


## Mục tiêu
Dự án xây dựng ứng dụng quản lý ký túc xá cần đáp ứng được những yêu cầu như sau:
- Quản lý thông tin sinh viên đang cư trú tại ký túc xá, bao gồm thông tin cá nhân, thời gian đăng ký, phòng ở, và quá trình thay đổi chỗ ở (chuyển phòng) một cách nhanh chóng và dễ dàng.
- Quản lý thông tin phòng ở, bao gồm số lượng chỗ trống, tình trạng phòng, tiện nghi kèm theo, giúp việc sắp xếp và bố trí chỗ ở cho sinh viên được thực hiện hợp lý và hiệu quả.
- Quản lý thông tin đăng ký chỗ ở theo từng đợt, xử lý đồng thời khi nhiều sinh viên đăng ký vào cùng một thời điểm, đảm bảo tính nhất quán và chính xác của dữ liệu.
- Quản lý thông tin dịch vụ trong ký túc xá như internet, giặt ủi, vệ sinh,… và hỗ trợ việc tính phí sử dụng các dịch vụ này một cách rõ ràng và minh bạch.
- Tính toán và quản lý chi phí sinh hoạt và các khoản phí phải đóng của từng sinh viên, hỗ trợ trích xuất hóa đơn và thống kê chi tiết.
- Quản lý thông tin nhân viên ký túc xá, bao gồm công việc, lịch làm việc, chấm công và công cụ tính lương theo hợp đồng hoặc ca làm việc.
- Tạo báo cáo tự động, chính xác và đầy đủ về tình hình sử dụng phòng, số lượng sinh viên cư trú, thu chi, sử dụng dịch vụ,…
- Cập nhật thông tin sinh viên, phòng ở và dịch vụ một cách nhanh chóng, kịp thời theo tình hình thực tế.


## Các chức năng chính trong ứng dụng

### Chức năng chính của Admin
- Đăng ký tài khoản
- Đăng nhập
- Thêm/Xóa/Sửa cơ sở dữ liệu
- Xem báo cáo & thống kê của ký túc xá
  
### Chức năng chính của sinh viên
- Đăng ký tài khoản
- Đăng nhập
- Thay đổi mật khẩu / Quên mật khẩu
- Đăng ký phòng ở
- Đăng ký dịch vụ
- Thanh toán
- Xem tình trạng hợp đồng, thẻ ký túc xá, thông tin,...

## Các ngôn ngữ công nghệ sử dụng
- Ngôn ngữ lập trình: Java/Java Swing
- IDE: NetBeans
- Cơ sở dữ liệu: Oracle
- Version Control System: Git, GitHub

## Yêu cầu hệ thống

## Hướng dẫn cài đặt chương trình

https://docs.google.com/document/d/1k3kFbOgPN_I3YDs3Q5WZ4hOdTFXHztLw/edit?usp=sharing&ouid=104544338019652150297&rtpof=true&sd=true

=======

## QUẢN LÝ KÝ TÚC XÁ


1. Clone the repository


2. Install IDE NetBeans


3. Install dependencies:
```
pip install --
```

4. Run the application locally
```
Start by run file "sigunup.java'
```


