import React, { useState } from 'react';
import { Flex, Grid, View } from '@adobe/react-spectrum';
import { Header } from './header/Header';
import { Sidebar } from './sidebar/Sidebar';
import { Route, Routes } from 'react-router-dom';
import { Account } from './account/Account';
import { AuctionList } from './auction-list/AuctionList';
import { MyAuctions } from './my-auctions/MyAuctions';
import { PurchaseHistory } from './purchase-history/PurchaseHistory';
import { EditAuction } from './auction/edit-auction/EditAuction';
import { UserContext } from './user.context';
import { useHttpQuery } from './http-query/use-http-query';
import { Auction } from './auction/Auction';
import { RecentlyViewed } from './recently-viewed/RecentlyViewed';
import { CategoriesContext } from './categories.context';
import { SearchContext } from './search.context';
import { Search } from './search/Search';
import { Home } from './home/Home';

export const App = () => {
    const [showSidebar, setShowSidebar] = useState(false);
    const [searchText, setSearchText] = useState();
    const { appResponse } = useHttpQuery('/accounts/me');
    const { appResponse: categoryResponse } = useHttpQuery('/categories');
    const categories = categoryResponse?.data || [];

    return (
        // The UserContext.Provider allows sub-components to easily access the logged in user
        <UserContext.Provider value={appResponse?.data}>
            <CategoriesContext.Provider value={categories}>
                <SearchContext.Provider value={searchText}>
                    <Grid
                        areas={[
                            'header header',
                            'sidebar content',
                            'footer footer',
                        ]}
                        columns={['.5fr', '3fr']}
                        rows={['size-1000', 'auto', 'size-1000']}
                        height="100vh"
                        backgroundColor="white"
                    >
                        <View backgroundColor="gray-300" gridArea="header">
                            <Header
                                menuClicked={() =>
                                    setShowSidebar((prev) => !prev)
                                }
                                searchValueUpdated={setSearchText}
                                searchValue={searchText}
                            ></Header>
                        </View>
                        {showSidebar && (
                            <View gridArea="sidebar" height="100%">
                                <Sidebar
                                    dismiss={() => setShowSidebar(false)}
                                />
                            </View>
                        )}
                        <View
                            backgroundColor="gray-100"
                            gridColumnStart={
                                showSidebar ? 'content' : 'sidebar'
                            }
                            gridColumnEnd="content"
                            overflow="scroll"
                        >
                            {/* Put new routes to views HERE */}
                            <Routes>
                                <Route index element={<Home />} />
                                <Route path="account*" element={<Account />} />
                                <Route
                                    path="auction/:auctionId*"
                                    element={<Auction />}
                                />
                                <Route
                                    path="myauctions"
                                    element={<MyAuctions />}
                                />
                                <Route
                                    path="purchases"
                                    element={<PurchaseHistory />}
                                />
                                <Route
                                    path="recentlyViewed*"
                                    element={<RecentlyViewed />}
                                />
                                <Route path="search*" element={<Search />} />
                                <Route
                                    path="create"
                                    element={
                                        <Flex
                                            direction="row"
                                            alignItems="start"
                                            justifyContent="center"
                                            margin="size-400"
                                            height="calc(100% - 80px)"
                                        >
                                            <EditAuction />
                                        </Flex>
                                    }
                                />
                            </Routes>
                        </View>
                        <View
                            backgroundColor="blue-700"
                            gridArea="footer"
                        ></View>
                    </Grid>
                </SearchContext.Provider>
            </CategoriesContext.Provider>
        </UserContext.Provider>
    );
};
