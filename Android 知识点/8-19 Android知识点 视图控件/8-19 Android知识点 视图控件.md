#视图——视图控件

##ViewPager的使用

ViewPager是android扩展包v4包中的类，这个类可以让用户左右切换当前的view。

- 布局文件

		<android.support.v4.view.ViewPager
	        android:id="@+id/viewPager"
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="1">
		</android.support.v4.view.ViewPager>

- 绑定适配器

		// 在Activity中
		viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		// Viewpager的适配器
		class MyAdapter extends FragmentStatePagerAdapter {
	
	        public MyAdapter(FragmentManager fm) {
	            super(fm);
	        }
	
	        @Override
	        public Fragment getItem(int position) {
	            return fragmentList.get(position);
	        }
	
	        @Override
	        public int getCount() {
	            return fragmentList.size();
	        }
	    }

##RecyclerView的使用

RecyclerView用于在有限的窗口中展示大量数据集，提供了一个耦合度更低的方式来复用ViewHolder，并且可以轻松的实现ListView、GridView以及瀑布流的效果。
 
- 布局文件

	    <android.support.v7.widget.RecyclerView
	        android:id="@+id/recyclerView"
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="1">
	    </android.support.v7.widget.RecyclerView>

- 设置布局管理器

	recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

- 设置Adapter

		recyclerView.setAdapter(myRecycleAdapter);

		class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {
	
	        @Override
	        public MyRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
	            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item, viewGroup, false));
	            return holder;
	        }
	
	        @Override
	        public void onBindViewHolder(MyRecycleAdapter.MyViewHolder viewHolder, int i) {
	            viewHolder.tv_item.setText(data.get(i));
	            viewHolder.tv_item.setHeight(new Random().nextInt(50) + 60);
	        }
	
	        @Override
	        public int getItemCount() {
	            return data.size();
	        }
	
	        public void addData(int position) {
		            data.add(position, "one");
	            notifyItemInserted(position);
	            notifyItemRangeChanged(position, data.size());
	        }
	

- 设置Item增加、移除动画

		recyclerView.setItemAnimator(new DefaultItemAnimator());

- 添加分割线
	
	    recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));

##DrawerLayout的使用

DrawerLayout是Support Library包中实现了侧滑菜单效果的控件，它带有滑动功能，左侧菜单（或者右侧）的展开与隐藏可以被DrawerLayout.DrawerListener的实现监听到。

- 布局文件
    
		<android.support.v7.widget.Toolbar
	        android:id="@+id/toolbar"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"></android.support.v7.widget.Toolbar>
	    <android.support.v4.widget.DrawerLayout
	        android:id="@+id/drawerLayout"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:background="#dddada"
	        >
	        <ListView
	            android:id="@+id/listView"
	            android:layout_width="240dp"
	            android:layout_height="match_parent"
	            android:layout_gravity="start"
	            android:background="#bebbbb"
	            android:choiceMode="singleChoice"
	            android:divider="@android:color/transparent"
	            android:dividerHeight="0dp" />
	    </android.support.v4.widget.DrawerLayout>

- 绑定布局

		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

- 设置Listview

		listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.item, R.id.tv_item, strings));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.setItemChecked(i, true);
                setTitle(strings[i]);
                drawerLayout.closeDrawer(listView);
                getSupportActionBar().setTitle(getTitle());
            }
        });

- 设置监听器

		mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getTitle());
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);



