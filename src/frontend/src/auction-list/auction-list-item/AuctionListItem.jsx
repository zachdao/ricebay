import React from 'react';
import { Flex, Text, View } from '@adobe/react-spectrum';
import styled from 'styled-components';
import Image from '@spectrum-icons/workflow/Image';

export const AuctionListItem = ({ auction }) => {
    return (
        <AuctionItemGrid>
            <View gridArea="image">
                {auction.images && auction.images.length ? (
                    <img src={auction.images[0]} alt="image of auction item" />
                ) : (
                    <View width="100%" height="100%" backgroundColor="gray-300">
                        <Image width="100%" height="100%" />
                    </View>
                )}
            </View>
            <Flex
                gridArea="content"
                justifyContent="start"
                alignItems="start"
                direction="column"
                margin="10px"
            >
                <LargerText>{`${auction.title} - $${
                    auction.currentBid || auction.minimumBid
                }`}</LargerText>
                <Text>
                    {auction.description.length > 60
                        ? auction.description.substring(0, 60) + '...'
                        : auction.description}
                </Text>
            </Flex>
        </AuctionItemGrid>
    );
};

const AuctionItemGrid = styled.div`
    grid-template-areas: 'image content';
    grid-template-columns: 1fr 3fr;
    display: grid;
    box-shadow: grey 2px 2px 3px 0px;
    border-radius: 3px;
    overflow: hidden;
    margin: 10px;
`;

const LargerText = styled.div`
    font-size: 125%;
    font-weight: bold;
`;
