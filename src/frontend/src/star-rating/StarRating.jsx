import React from 'react';
import Star from '@spectrum-icons/workflow/Star';
import StarOutline from '@spectrum-icons/workflow/StarOutline';
import { Flex } from '@adobe/react-spectrum';
import styled from 'styled-components';

export const StarRating = ({ rating, onPress, ...otherProps }) => {
    // TODO: Figure out half stars?
    const stars = Array(5)
        .fill()
        .map((_, i) => (
            <RatingStar
                key={i}
                onClick={onPress ? () => onPress(i + 1) : undefined}
            >
                {i < rating ? <Star /> : <StarOutline />}
            </RatingStar>
        ));
    return (
        <Flex direction="row" {...otherProps}>
            {rating === 0 && !onPress ? <em>Seller not rated</em> : stars}
        </Flex>
    );
};

const RatingStar = styled.div`
    cursor: ${(props) => (props.onClick ? 'pointer' : 'unset')};
`;
