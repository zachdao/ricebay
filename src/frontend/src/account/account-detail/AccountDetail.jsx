import React from 'react';
import { Flex, Grid, Heading, Text, View } from '@adobe/react-spectrum';
import { UserProfile } from '../../user-profile/UserProfile';
import styled from 'styled-components';
import { HiddenValue } from '../../hidden-value/HiddenValue';
import { StarRating } from '../../star-rating/StarRating';

/**
 * Render the details of an account
 * @param account account to render (type Account)
 */
export const AccountDetail = ({ account }) => {
    return (
        <Grid
            areas={['image form']}
            columns={['1fr', '1fr']}
            alignItems="start"
            justifyContent="start"
        >
            <View gridArea="image">
                <UserProfile src={account.image} />
            </View>
            <Flex
                gridArea="form"
                direction="column"
                alignItems="start"
                justifyContent="start"
            >
                <Heading level="4">Personal Information</Heading>
                {/* Name and alias*/}
                <LargeText>
                    {`${account.givenName} ${account.surname} (${account.alias})`}
                </LargeText>
                {/* email */}
                <Text>{account.email}</Text>
                <Heading level="4">Your Seller Rating</Heading>
                {account.rating ? (
                    <StarRating rating={account.rating} />
                ) : (
                    <em>Not Rated</em>
                )}
                <Heading level="4">Payment Information</Heading>
                <HiddenValue value={account.zelleId} />
            </Flex>
        </Grid>
    );
};

const LargeText = styled.div`
    font-size: 175%;
    font-weight: lighter;
`;
