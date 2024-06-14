package com.example.mobileproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private ListView newsListView;
    private List<News> newsList;
    private NewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        newsListView = view.findViewById(R.id.news_listview);

        newsList = new ArrayList<>();
        // Sample news data
        newsList.add(new News("주사 한 방에 48억원…세계에서 가장 비싼 약 알아보니", R.drawable.pfizer,
                "글로벌 제약사 화이자가 개발한 B형 혈우병 유전자치료제가 미국 식품의약국(FDA) 승인을 받았습니다. 치료제 가격이 무려 48억원에 달합니다.\n" +
                        "\n" +
                        "화이자는 FDA가 이 회사의 B형 혈우병 유전자치료제 베크베즈(Beqvez, 피다나코진 엘라파보벡)를 승인했다고 지난 26일 (현지시각) 밝혔습니다. \n" +
                        "\n" +
                        "베크베즈의 치료비는 350만달러(약 48억2125만원)로 알려졌습니다. 이는 현재까지 전세계에서 가장 비싼 약인 호주 제약사 CSL베링의 헴제닉스와 동일한 가격입니다. 지난 2022년 승인 당시 약값이 350만달러였습니다. \n" +
                        "\n" +
                        "두 치료제 모두 B형 혈우병치료제입니다. 전세계 B형 혈우병 환자는 약 3만8000명으로 알려져 있습니다.\n" +
                        "\n" +
                        "혈우병은 혈액 내 응고인자라고 불리는 단백질 생성 조절 유전자의 결핍으로 발생하는 출혈성 질환입니다. 결핍 인자에 따라 크게 혈우병A와 혈우병B 등으로 분류하는데, A는 제8혈액응고인자가 결핍된 것이고, B는 제9혈액응고인자가 결핍된 것입니다. \n" +
                        "\n" +
                        "혈우병 유전자치료제 가격이 비싼 이유는 일주일 또는 한 달에 여러 차례 정기적으로 혈액응고인자를 정맥 주입해야 하는 기존 표준 치료법과 달리, 한 번 주입으로 결핍된 혈액응고인자를 스스로 생산할 수 있게 되는 획기적인 방식이기 때문입니다. \n" +
                        "\n" +
                        "미국에서는 전형적인 혈우병B 환자가 성인기에 치료를 위해 쓰는 의료비용은 2000만달러(약 260억원) 이상으로 알려졌습니다. "));

        newsList.add(new News("코카콜라에 원래는 진짜 '코카잎'이 들어갔다고?", R.drawable.coca,
                "남북전쟁이 막 끝난, 1886년 당시 미국에는 독특한 문화가 있었습니다. 성분이나 효능이 입증되지 않은 약들이 '신비의 명약'이라는 이름을 달고 불티나게 팔린 겁니다. 전쟁 이후 아픈 사람은 많은데 제대로 된 의학 지식이나 의사는 부족하다 보니 나타난 현상이었죠. 그때 가장 인기 있던 상품 중 하나가 '뱅 마리아니'라는 제품이었습니다. 프렌치 와인에 코카 잎을 담가서 만든 건데, 이게 워낙 잘나가다 보니 모방제품들이 줄줄이 나옵니다.\n" +
                        "\n" +
                        "그중 하나가 코카콜라의 전신으로 평가받는 '프렌치 와인 코카'였습니다. 조지아주 애틀랜타에서 약을 만들어 팔던 존 펨버턴이 뱅 마리아니의 주성분에 콜라 열매 추출물을 첨가해 만든 제품이었는데요. 이걸로 막 돈을 벌어보려던 찰나, 금주법이 도입됩니다. 그래서 프렌치 와인 코카에서 술을 제거하고 설탕을 넣어 만들어낸 게 코카콜라입니다. 이후 코카인 성분이 문제가 되자 1903년에는 코카인 성분도 완전히 제거하게 됩니다."));
        newsList.add(new News("밸런타인데이에 초콜릿을 선물 할 수밖에 없는 이유", R.drawable.chocolate,
                "어느새 밸런타인데이다. ‘2월14일’은 언제부터인가 초콜릿을 주고받으며 사랑하는 마음을 전하는 날로 굳어졌다. 처음에는 일본 제과업체에서 자사 제품 판매 확대를 위해 시작됐지만 매년 2월14일은 서로 사랑하는 사람에게 초콜릿을 줌으로써 마음도 함께 전하는 하나의 문화로 자리매김하게 됐다.\n" +
                        " \n" +
                        "그런데 왜 하필 초콜릿을 선물하는 걸까. 선물은 상징이기도 하다. 물론 밸런타인데이에 ‘당연하게’ 초콜릿을 선물하는 것은 초콜릿을 사랑의 상징으로 만들어버린 제과업체의 상술이라고 볼 수도 있다.\n" +
                        " \n" +
                        "하지만 상술을 떠나 실제로 초콜릿은 꽤 로맨틱한 음식이다. 이는 단순히 달콤한 맛 때문만은 아니다. 초콜릿에는 ‘페닐에틸아민’이라는 물질이 들어있어 ‘누군가를 사랑할 때의 감정’을 불러일으키는 효과가 있다. 바람둥이로 유명한 카사노바도 여인들을 유혹할 때 초콜릿을 즐겨 선물했다. ‘사랑의 묘약’이라고도 불리는 페닐에틸아민이 우리에게 어떤 영향을 주는지 알아봤다.\n" +
                        "\n" +
                        "을지대학병원 가정의학과 박창해 교수는 “초콜릿에 많이 들어있는 페닐에틸아민은 사람이 사랑에 빠졌을 때 뇌가 분비하는 화학물질과 성분이 같다”며 “몸과 마음을 편안하게 하면서 심장박동을 올려줘 마치 사랑에 빠졌을 때 가슴이 두근두근 뛰는 것 같은 착각과 행복한 기분이 들게 한다”고 말했다. 초콜릿은 페닐에틸아민이 포함된 대표적 음식으로 보통 100g에 약 50~100mg정도의 페닐에틸아민이 포함돼 있다.\n" +
                        "\n" +
                        "이런 까닭에 서양학자들은 초콜릿이 애정생활에 도움이 된다고 말한다. 초콜릿은 로마시대부터 연인끼리 애정을 표현할 때 선물하는 음식이었다\n"));

        adapter = new NewsAdapter(getContext(), newsList);
        newsListView.setAdapter(adapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News selectedNews = newsList.get(position);

                NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
                Bundle b = new Bundle();
                b.putString("news_title", selectedNews.getTitle());
                b.putInt("news_image", selectedNews.getImageResId());
                b.putString("news_content", selectedNews.getContent());
                newsDetailFragment.setArguments(b);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, newsDetailFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    private class NewsAdapter extends ArrayAdapter<News> {
        private Context context;
        private List<News> newsList;

        public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
            super(context, 0, objects);
            this.context = context;
            this.newsList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            News news = getItem(position);

            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);

            ImageView img = convertView.findViewById(R.id.news_image);
            TextView title = convertView.findViewById(R.id.news_title);

            title.setText(news.getTitle());
            img.setImageResource(news.getImageResId());

            return convertView;
        }
    }

    private class News {
        private String title;
        private int imageResId;
        private String content;

        public News(String title, int imageResId, String content) {
            this.title = title;
            this.imageResId = imageResId;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public int getImageResId() {
            return imageResId;
        }

        public String getContent() {
            return content;
        }
    }
}
