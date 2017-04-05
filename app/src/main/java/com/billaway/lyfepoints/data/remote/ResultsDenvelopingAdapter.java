package com.billaway.lyfepoints.data.remote;


import com.billaway.lyfepoints.data.models.BaseResult;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ResultsDenvelopingAdapter extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Type baseResponseType = TypeToken.getParameterized(BaseResult.class, type).getType();
        final Converter<ResponseBody, BaseResult> delegate =
                retrofit.nextResponseBodyConverter(this, baseResponseType, annotations);
        return new ResultsResponseBodyConverter<>(delegate);
    }

    private static final class ResultsResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        final Converter<ResponseBody, BaseResult> deligate;

        ResultsResponseBodyConverter(Converter<ResponseBody, BaseResult> deligate) {
            this.deligate = deligate;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T convert(ResponseBody value) throws IOException {
            BaseResult<T> baseResult = deligate.convert(value);
            return baseResult.getResults();
        }
    }
}
