package site.blbc.bean;

/**
 * 图片上传的组件
 */
public class UploadImage {

    // 表达了一种依赖关系：UploadImage 依赖 AccountCheck
    private AccountCheck accountCheck;

    public void setAccountCheck(AccountCheck accountCheck) {
        this.accountCheck = accountCheck;
    }

    public void upload(String image) {
        accountCheck.check();
        System.out.println("uploading " + image);
    }

//    public static void main(String[] args) {
//        UploadImage uploadImage = new UploadImage();
//        uploadImage.upload("bd.jpg");
//    }
}
