import React, { useState } from 'react';
import { Grid, View } from '@adobe/react-spectrum';
import { Header } from './header/Header';
import { Sidebar } from './sidebar/Sidebar';
import { Route, Routes } from 'react-router-dom';
import { Account } from './account/Account';
import { AuctionView } from './auction-view/AuctionView';
import { AuctionList } from './auction-list/AuctionList';
import { MyAuctions } from './my-auctions/MyAuctions';
import { PurchaseHistory } from './purchase-history/PurchaseHistory';
import { CreateAuction } from './create-auction/CreateAuction';
import { UserContext } from './user.context';
import { useHttpQuery } from './http-query/use-http-query';

export const App = () => {
    const [showSidebar, setShowSidebar] = useState(false);
    const { appResponse, error, status } = useHttpQuery('/accounts/me');

    return (
        <UserContext.Provider value={appResponse?.data}>
            <Grid
                areas={['header header', 'sidebar content', 'footer footer']}
                columns={['.5fr', '3fr']}
                rows={['size-1000', 'auto', 'size-1000']}
                height="100vh"
                backgroundColor="white"
            >
                <View backgroundColor="gray-300" gridArea="header">
                    <Header
                        menuClicked={() => setShowSidebar((prev) => !prev)}
                    ></Header>
                </View>
                {showSidebar && (
                    <View gridArea="sidebar" height="100%">
                        <Sidebar dismiss={() => setShowSidebar(false)} />
                    </View>
                )}
                <View
                    backgroundColor="gray-100"
                    gridColumnStart={showSidebar ? 'content' : 'sidebar'}
                    gridColumnEnd="content"
                    overflow="scroll"
                >
                    {/* Put new routes to views HERE */}
                    <Routes>
                        <Route
                            path="auction/:auctionId"
                            element={<AuctionView />}
                        />
                        <Route index element={<AuctionList />} />
                        <Route path="account*" element={<Account />} />
                        <Route path="myauctions" element={<MyAuctions />} />
                        <Route path="purchases" element={<PurchaseHistory />} />
                        <Route path="create" element={<CreateAuction />} />
                    </Routes>
                </View>
                <View backgroundColor="blue-700" gridArea="footer"></View>
            </Grid>
        </UserContext.Provider>
    );
};
