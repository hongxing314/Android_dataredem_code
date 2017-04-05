package com.billaway.lyfepoints.phonenumber;


import com.billaway.lyfepoints.BasePresenter;
import com.billaway.lyfepoints.BaseView;
import com.billaway.lyfepoints.data.models.Coupon;

class PhoneNumberContract {
    interface Presenter extends BasePresenter {

        void onPhoneNumberSubmit();

        void onShare();

        void goHome();

        void onCreateSponsorship();
    }

    interface View extends BaseView<PhoneNumberContract.Presenter> {

        void showCouponLoading();

        void showCoupon(Coupon coupon);

        void showCouponLoadingError();

        void showPhoneNumberSubmitProgress();

        void showPhoneNumberSubmitError();

        void showPhoneNumberSubmitSuccess(Coupon coupon);

        void showCreateSponsorship(Coupon coupon);
    }


    static class HomeScreenRequest {

    }

    static class CreateSponsorshipEvent{
        public final Coupon coupon;

        CreateSponsorshipEvent(Coupon coupon){
            this.coupon = coupon;
        }
    }

    static class ShareRequestEvent{
        public final Coupon coupon;

        ShareRequestEvent(Coupon coupon) {
            this.coupon = coupon;
        }
    }
}
